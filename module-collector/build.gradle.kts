dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // MockWebServer for testing
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}