package com.cardgames.demo.services.impl;

import com.cardgames.demo.model.GameType;
import com.cardgames.demo.model.User;
import com.cardgames.demo.repository.UserRepository;
import com.cardgames.demo.services.UserServiceV1;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceV1Impl implements UserServiceV1 {

    public static final String MOCK_USER_LOGIN = "user1";

    private final UserRepository userRepository;

    @PostConstruct
    private void init() { //todo: after implementing authorization and init sql scripts
        if (userRepository.findUserByLogin(MOCK_USER_LOGIN).isEmpty()) {
            User user = new User();
            user.setGamesPlayed(0);
            user.setPenalty(0);
            user.setName("John");
            user.setAge(21);
            user.setLogin(MOCK_USER_LOGIN);

            userRepository.save(user);
        }
    }

    @Override
    public void updateUserStatistic(GameType gameType, boolean winner) {
        User user = getActiveUser();

        increaseGamesPlayedNumber(user);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void applyPenalty(GameType game) {
        User user = getActiveUser();

        //process user's scores
        user.setPenalty(user.getPenalty() + 1);
        increaseGamesPlayedNumber(user);

        userRepository.save(user);
    }

    @Override
    public Long getActiveUserId() {
        return getActiveUser().getId();
    }

    private void increaseGamesPlayedNumber(User user) {
        user.setGamesPlayed(user.getGamesPlayed() + 1);
    }

    private User getActiveUser() { //todo: implement authorization flow
        return userRepository.findUserByLogin("user1").get();
    }
}
