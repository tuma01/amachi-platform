package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.auth.repository.UserAccountRepository;
import com.amachi.app.core.auth.service.UserAccountService;
import com.amachi.app.core.common.annotation.TenantAware;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public Optional<UserAccount> findByUserAndTenantCode(User user, String tenantCode) {
        return userAccountRepository.findByUserAndTenantCode(user, tenantCode);
    }
}
