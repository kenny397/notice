package com.jun.nautilus.server.mvc.service

import com.jun.nautilus.domain.AuthManager
import com.jun.nautilus.domain.UserService
import com.jun.nautilus.server.mvc.controller.LoginRequest
import com.jun.nautilus.server.mvc.controller.RegisterRequest
import com.jun.nautilus.server.mvc.controller.view.AuthInfo
import com.jun.nautilus.server.mvc.security.InvalidAuthTokenException
import com.jun.nautilus.server.mvc.security.JwtTokenManager
import com.jun.nautilus.server.mvc.security.TokenType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService (
    private val authManager: AuthManager,
    private val userService: UserService,
    private val jwtTokenManager: JwtTokenManager
)
{

    @Transactional(readOnly = true)
    fun login(loginRequest: LoginRequest): AuthInfo {
        if(authManager.login(loginRequest.email,loginRequest.password)){
            val user = userService.findByEmail(loginRequest.email)
            val userId = user.id
            val userName = user.name
            val accessToken= jwtTokenManager.createAccessToken( userId )
            val refreshToken= jwtTokenManager.createRefreshToken(userId)
            return AuthInfo(userId,userName,accessToken,refreshToken)
        }else{
            throw IllegalArgumentException("잘못된 입력입니다")
        }

    }

    @Transactional
    fun register(registerRequest: RegisterRequest): String{
        val id =userService.create(registerRequest.name,registerRequest.email).id
        return authManager.register(id,registerRequest.email,registerRequest.password).userId
    }

    @Transactional(readOnly = true)
    fun reissueToken(refreshToken: String): AuthInfo{
        val verification = jwtTokenManager.verifyToken(refreshToken)
        // early exit
        if (verification.type!=TokenType.Refresh) {
            throw BadCredentialsException("유효하지 않는 토큰 정보입니다")
        }
        val userId = verification.userId
        val userName = userService.findById(userId).name
        val accessToken = jwtTokenManager.createAccessToken(userId)
        return AuthInfo(userId,userName,accessToken,refreshToken)

    }


}