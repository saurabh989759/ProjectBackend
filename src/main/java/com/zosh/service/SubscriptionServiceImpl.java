package com.zosh.service;

import com.zosh.domain.PlanType;
import com.zosh.domain.SubscriptionType;
import com.zosh.model.Subscription;
import com.zosh.model.User;
import com.zosh.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
    }

    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription=new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12)); // Assuming one month validity for simplicity
        subscription.setValid(true);
        subscription.setSubscriptiontype(SubscriptionType.FREE);
        return subscription;
    }

    @Override
    public Subscription getUserSubscription(Long userId) throws Exception {
       Subscription subscription = subscriptionRepository.findByUserId(userId);
       if(subscription==null){
           throw new Exception("subscription not found with userId "+userId);
       }
       subscription.setValid(isValid(subscription));
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription upgradeSubscription(Long userId, PlanType planType) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);

        subscription.setSubscriptiontype(SubscriptionType.PAID);
        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setValid(true);
        if(planType.equals(PlanType.ANNUALLY)){
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }
        else {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {

        if(subscription.getSubscriptiontype().equals(SubscriptionType.FREE)){
            return true;
        }
        LocalDate endDate = subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        if (endDate.isBefore(currentDate) ||  endDate.isEqual(currentDate)) {
            return true;
        } else {
            return false;
        }


    }
}