package ru.ershov.project.orderservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.ershov.project.common.models.Customer;
import ru.ershov.project.common.models.User;
import ru.ershov.project.common.models.statuses.UserRole;
import ru.ershov.project.orderservice.dto.auth.LoginDTO;
import ru.ershov.project.orderservice.dto.auth.LoginResponseDTO;
import ru.ershov.project.orderservice.dto.auth.RegisterDTO;
import ru.ershov.project.orderservice.dto.auth.RegisterResponseDTO;
import ru.ershov.project.orderservice.mapper.authMapper.RegisterDTOMapper;
import ru.ershov.project.orderservice.repositories.CustomerRepository;
import ru.ershov.project.orderservice.security.JWTUtil;
import ru.ershov.project.orderservice.util.RandomCoordinatesGenerator;
import ru.ershov.project.orderservice.util.RegisterValidator;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final RegisterDTOMapper registerDTOMapper;
    private final RegisterValidator validator;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponseDTO register(RegisterDTO dto, BindingResult bindingResult) {
        validator.validate(dto, bindingResult);
        Customer customerToAdd = registerDTOMapper.toEntity(dto);
        enrichCustomer(customerToAdd);
        Customer savedCustomer = customerRepository.save(customerToAdd);
        String token = jwtUtil.generateToken(dto.getUsername());
        return new RegisterResponseDTO().setUserId(savedCustomer.getId())
                .setMessage("Successfully registered")
                .setRegisterTime(LocalDateTime.now())
                .setJwtToken(token);
    }

    public LoginResponseDTO login(LoginDTO dto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }

        String token = jwtUtil.generateToken(dto.getUsername());
        return new LoginResponseDTO(token);
    }

    private void enrichCustomer(Customer customerToEnrich) {
        customerToEnrich.setCoordinates(RandomCoordinatesGenerator.generateCoordinates());
        User userToAdd = customerToEnrich.getUser();
        userToAdd.setRole(UserRole.ROLE_CUSTOMER)
                .setPassword(passwordEncoder.encode(userToAdd.getPassword()))
                .setCreatedAt(LocalDateTime.now())
                .setCustomer(customerToEnrich);
    }
}