package com.zosh.controller;

import com.zosh.exception.UserException;
import com.zosh.model.User;
import com.zosh.service.UserService;
import org.springframework.web.bind.annotation.RestController;


    import com.zosh.domain.PlanType;
import com.zosh.model.Subscription;
import com.zosh.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/subscriptions")
    public class SubscriptionController {

        @Autowired
        private SubscriptionService subscriptionService;

        @Autowired
        private UserService userService;



//        @PostMapping("/create")
//        public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
//            Subscription createdSubscription = subscriptionService.createSubscription(subscription);
//            return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
//        }

        @GetMapping("/user")
        public ResponseEntity<Subscription> getUserSubscription(
                @RequestHeader("Authorization") String jwt) throws Exception {
            User user = userService.findUserProfileByJwt(jwt);
            Subscription userSubscription = subscriptionService.getUserSubscription(user.getId());

            if (userSubscription != null) {
                return new ResponseEntity<>(userSubscription, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @PatchMapping("/upgrade")
        public ResponseEntity<Subscription> upgradeSubscription(@RequestHeader("Authorization") String jwt,
                                                                @RequestParam PlanType planType) throws UserException {
            User user = userService.findUserProfileByJwt(jwt);
            Subscription upgradedSubscription = subscriptionService.upgradeSubscription(user.getId(), planType);

                return new ResponseEntity<>(upgradedSubscription, HttpStatus.OK);

        }

//        @GetMapping("/isValid/{subscriptionId}")
//        public ResponseEntity<Boolean> isValidSubscription(@PathVariable Long subscriptionId) {
//            Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
//            if (subscription != null) {
//                boolean isValid = subscriptionService.isValid(subscription);
//                return new ResponseEntity<>(isValid, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
    }


