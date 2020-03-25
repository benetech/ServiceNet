package org.benetech.servicenet;

import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class used only for tests, with some methods mocked
 */
@Service
@Transactional
public class TestUserService extends UserService {

    private static final String ADMIN_LOGIN = "admin";

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> getCurrentUserOptional() {
        return userProfileRepository.findOneByLogin(ADMIN_LOGIN);
    }

    @Override
    public Optional<SystemAccount> getCurrentSystemAccount() {
        return userProfileRepository.findOneByLogin(ADMIN_LOGIN).map(UserProfile::getSystemAccount);
    }
}

