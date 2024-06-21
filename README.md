# spring-security-jwt
Authentication basics with Json Web Tokens (jwt) in Spring Boot (dependencies like jjwt can be found in the pom.xml) 
Every http request goes through the JwtFilter first which implements the OncePerRequestFilter (since addFilterBefore() is used in the
SecurityFilterChain in JwtConfig), it extracts the Bearer token and extracts the username from it. At this point itself, if the jwt has
expired or invalid or signature doesn't match ot anything, an exception will be thrown and authentication will fail automatically.
A confusion can stem from here as validateToken() in JwtUtil by you wasn't even used in the JwtFilter, still expired or invalid
tokens are detected. So from run n debugging through it, it seems the Jwts.parser() which extracts All Claim(s) (it has to be done
to extract the username), validates the token first before extracting the claims or subject (username here). Still validateToken() method 
can be used for double or custom validation I guess. 

Rest of the security implementation is similar to spring-security-jdbc repo of yours. AuthenticationEntryPoint just tells the user
what to do or alerts user if unauthorized - part of exception handling

Also since it doesn't use WebSecurityConfigurer (deprecated), to configure the AuthenticationManager, you used the configureAuthenticationManager(HttpSecurity http) in JwtConfig, which seems to get the shared AuthenticationManagerBuilder in the
security configuration context and use it to configure the AuthenticationManager like specifying the UserDetailsService implementation
which has the important loadByUsername() method which AuthenticationManager uses to get UserDetails from DB and match username and password
against the loginRequest for authentication AND password encoder (it has to be as specific as possible when you Autowire the @Bean passwordEncoder())

Also UsernamePasswordAuthenticationToken is used by AuthenticationManager as just an input for authentication (as in the name)
It's also used by the JwtFiter to set the SecurityContext upon successful authentication, it uses a different constructor then tho!

