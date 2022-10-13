package com.jun.nautilus.domain.testhelper

import com.jun.nautilus.domain.App
import com.jun.nautilus.domain.AuthUser
import com.jun.nautilus.domain.Notification
import com.jun.nautilus.domain.User
import io.mockk.coVerify
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID




fun anApp(
    id: String = "testAppId",
    name: String = "testApp",
    owners: Set<User> = setOf<User>(anUser())
): com.jun.nautilus.domain.App = TestApp(id,name, owners)

private data class TestApp(
    override val id: String,
    override val name: String,
    override val owners: Set<User>
): com.jun.nautilus.domain.App


fun anUser(
    id : String = "testAppId",
    name : String = "john.doe",
    email : String = "kenny@jun.corp"
) : User = TestUser(id,name,email)

private data  class TestUser(
    override val id: String ,
    override val name: String,
    override val email: String
) : User

fun anAuthUser(
    userId: String = "test",
    email: String = "jun@jun.corp",
    password: String = "aaaaaa"
):AuthUser = TestAuthUser(userId,email, password )

private data class TestAuthUser(
    override val userId: String,
    override val email: String,
    override val password: String
): AuthUser

fun anNotification(
    id: String = "testAppId",
    title: String = "testTitle",
    content: String = "testContent",
    publishedAt: Instant = Instant.now(),
    app: com.jun.nautilus.domain.App = anApp(),
    active: Boolean = true
): Notification = TestNotification(id,title, content, publishedAt, app, active)
private data class TestNotification(
    override val id: String,
    override val title: String,
    override val content: String,
    override val publishedAt: Instant,
    override val app: com.jun.nautilus.domain.App,
    override val active: Boolean
): Notification{
    override val createdAt: Instant = Instant.now()
}
