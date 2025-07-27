package com.marketplace.MarketBack.service;

import ch.qos.logback.core.util.StringUtil;
import com.marketplace.MarketBack.controller.dto.CreateReputationDto;
import com.marketplace.MarketBack.controller.dto.FeaturedSeller;
import com.marketplace.MarketBack.controller.dto.RatingAndCommentsUserDto;
import com.marketplace.MarketBack.controller.dto.ResponseReputationDto;
import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.ReputationEntity;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.entity.UserImageEntity;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import com.marketplace.MarketBack.persistence.repository.ReputationRepository;
import com.marketplace.MarketBack.persistence.repository.UserImageRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReputationService {

    @Autowired
    private ReputationRepository reputationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    // Create a new reputation
    public ResponseReputationDto createReputation(Long productId, int rating, Authentication auth, String comment) {
        // User who is creating the reputation
        UserEntity raterUser = userRepository.findUserEntityByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));

        // Get the product
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_NOT_FOUND));

        // User who is being rated
        UserEntity ratedUser = product.getUser();

        // Check if the user has already rated this product, if the user has already rated the product, update the reputation instead of creating a new one
        if (reputationRepository.existsByRaterUserAndProduct(raterUser, product)) {
            ReputationEntity existingReputation = reputationRepository.findByRaterUserAndProduct(raterUser, product);
            existingReputation.setRating(rating);
            existingReputation.setComment(comment);
            ReputationEntity reputationSaved = reputationRepository.save(existingReputation);
            return new ResponseReputationDto()
                    .builder()
                    .id(reputationSaved.getId())
                    .productId(reputationSaved.getProduct().getId())
                    .rating(reputationSaved.getRating())
                    .comment(reputationSaved.getComment())
                    .build();
        } else {
            // Create a new reputation
            ReputationEntity reputation = new ReputationEntity();
            reputation.setRaterUser(raterUser);
            reputation.setRatedUser(ratedUser);
            reputation.setProduct(product);
            reputation.setRating(rating);
            reputation.setComment(comment);

            ReputationEntity reputationSaved = reputationRepository.save(reputation);
            return new ResponseReputationDto()
                    .builder()
                    .id(reputationSaved.getId())
                    .productId(reputation.getProduct().getId())
                    .rating(reputation.getRating())
                    .comment(reputation.getComment())
                    .build();
        }


    }

    // Method for get reputation
    public List<ResponseReputationDto> getReputation(Long productId){
        return reputationRepository.findByProductId(productId).stream()
                .map(reputation -> {
                    return new ResponseReputationDto().builder()
                        .id(reputation.getId())
                        .productId(reputation.getProduct().getId())
                        .rating(reputation.getRating())
                        .comment(reputation.getComment())
                        .raterUserName(reputation.getRaterUser().getName())
                        .raterUserUsername(reputation.getRaterUser().getUsername())
                        .raterUserAvatar(userImageRepository.findUserImageEntityByUserId(reputation.getRaterUser().getId())
                                .stream().findFirst()
                                .map(UserImageEntity::getUrl)
                                .orElse(null))
                        .build();
                }).toList();
    }

    // Function to calculate the average rating of a seller based on their reputations
    public double calculateAverageRating(Long userId) {
        List<ReputationEntity> reputations = reputationRepository.findByRatedUserId(userId);
        if (reputations.isEmpty()) {
            return 0.0; // No reputations found, return 0
        }
        double totalRating = reputations.stream().mapToInt(ReputationEntity::getRating).sum();
        return totalRating / reputations.size(); // Calculate average rating
    }

    public List<FeaturedSeller> getFeaturedSellers() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new NotFoundException("No users found", ErrorCode.USER_NOT_FOUND);
        }
        // Calculate the average rating for each user using this.calculateAverageRating() and return a list of FeaturedSeller DTOs
        List<FeaturedSeller> allSellers =  users.stream().map(user -> {
            return FeaturedSeller.builder()
                    .id(user.getId())
                    .name(StringUtil.isNullOrEmpty(user.getName())? user.getUsername(): user.getName())
                    .rating(this.calculateAverageRating(user.getId()))
                    .avatarUrl(userImageRepository.findUserImageEntityByUserId(user.getId())
                        .stream().findFirst()
                        .map(UserImageEntity::getUrl)
                        .orElse(null))
                    .build();

        }).toList();

        // Return the list of FeaturedSeller DTOs
        return allSellers.stream()
                .filter(seller -> seller.getRating() > 0) // Filter out sellers with no ratings
                .sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating())) // Sort by rating in descending order
                .limit(3) // Limit to top 10 sellers
                .toList();

    }

    // Method to get the average rating and comments of a user
    public RatingAndCommentsUserDto getUserRating(Long userId) {
        try{
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND));

            List<ReputationEntity> reputations = reputationRepository.findByRatedUser(user);
            if (reputations.isEmpty()) {
                throw new NotFoundException("No reputations found for this user", ErrorCode.REPUTATION_NOT_FOUND);
            }

        double averageRating = reputations.stream()
                .mapToInt(ReputationEntity::getRating)
                .average()
                .orElse(0.0);

            List<ResponseReputationDto> reputationsUser = reputations.stream()
                    .map(reputation -> new ResponseReputationDto()
                            .builder()
                            .id(reputation.getId())
                            .productId(reputation.getProduct().getId())
                            .rating(reputation.getRating())
                            .comment(reputation.getComment())
                            .raterUserName(reputation.getRaterUser().getName())
                            .raterUserUsername(reputation.getRaterUser().getUsername())
                            .raterUserAvatar(userImageRepository.findUserImageEntityByUserId(reputation.getRaterUser().getId())
                                    .stream().findFirst()
                                    .map(UserImageEntity::getUrl)
                                    .orElse(null))
                            .build())
                    .toList();

            return RatingAndCommentsUserDto.builder()
                    .averageRating(averageRating)
                    .reputations(reputationsUser)
                    .build();
        }catch (Exception e){
            throw new NotFoundException("User not found", ErrorCode.USER_NOT_FOUND);
        }

    }


}
