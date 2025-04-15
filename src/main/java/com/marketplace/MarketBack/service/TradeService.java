package com.marketplace.MarketBack.service;

import com.marketplace.MarketBack.exception.custom.NotFoundException;
import com.marketplace.MarketBack.exception.enums.ErrorCode;
import com.marketplace.MarketBack.persistence.entity.ProductEntity;
import com.marketplace.MarketBack.persistence.entity.TradeRequestEntity;
import com.marketplace.MarketBack.persistence.entity.TradeStatus;
import com.marketplace.MarketBack.persistence.entity.UserEntity;
import com.marketplace.MarketBack.persistence.repository.ProductRepository;
import com.marketplace.MarketBack.persistence.repository.TradeRepository;
import com.marketplace.MarketBack.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public TradeRequestEntity requestTrade(Long receiverId,Long requestedProductId, Long offeredProductId, Authentication auth) {
        UserEntity requester = userRepository.findUserEntityByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        ProductEntity offered = productRepository.findById(offeredProductId)
                .orElseThrow(() -> new RuntimeException("Offered product not found"));


        //Get requested product and user who receive
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        ProductEntity requested = productRepository.findById(requestedProductId)
                .orElseThrow(() -> new RuntimeException("Requested product not found"));

        if(!offered.getUser().getId().equals(requester.getId()) || !receiver.getId().equals(requested.getUser().getId())){
            throw new RuntimeException("El producto no pertenece al usuario");
        }
        TradeRequestEntity trade = TradeRequestEntity.builder()
                .requester(requester)
                .receiver(receiver)
                .offeredProduct(offered)
                .requestedProduct(requested)
                .build();

        return tradeRepository.save(trade);
    }

    public List<TradeRequestEntity> getUserTrades(Long userId) {
        return tradeRepository.findByRequesterIdOrReceiverId(userId, userId);
    }

    //Get trades by requester id(get the request that i've made)
    public List<TradeRequestEntity> getTradeByRequesterId(Authentication authentication) {
        UserEntity userReceiver = userRepository.findUserEntityByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado", ErrorCode.USER_NOT_FOUND));
        return tradeRepository.findByRequesterId(userReceiver.getId());

    }

    //Get trades by receiver id(get my offers)
    public List<TradeRequestEntity> getTradeByReceiverId(Authentication authentication){
        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName()).orElseThrow(()-> new RuntimeException("No existe el usario"));
        return tradeRepository.findByReceiverId(user.getId());

    }


    // update the status (pending, accepted, rejected)
    public TradeRequestEntity updateTradeStatus(Long tradeId, TradeStatus status) {
        TradeRequestEntity trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trade not found"));

        trade.setStatus(status);
        return tradeRepository.save(trade);
    }

    public TradeRequestEntity getTradeById(Long tradeId) {
        return tradeRepository.findById(tradeId).orElseThrow(() -> new NotFoundException("Trade doesn't exists",ErrorCode.TRADE_NOT_FOUND));
    }
}
