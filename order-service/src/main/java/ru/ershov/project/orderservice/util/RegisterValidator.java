package ru.ershov.project.orderservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ershov.project.common.util.ErrorsUtil;
import ru.ershov.project.orderservice.dto.auth.RegisterDTO;
import ru.ershov.project.orderservice.repositories.CustomerRepository;
import ru.ershov.project.orderservice.repositories.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegisterValidator implements Validator {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public void validate(Object o, Errors errors) {
        RegisterDTO dto = (RegisterDTO) o;
        Optional<String> phone = customerRepository.findCustomerByPhone(dto.getPhone());

        if (phone.isPresent()) {
            errors.rejectValue("phone", "400",
                    "A user with this phone number is already registered");
        }

        Optional<String> email = customerRepository.findCustomerByEmail(dto.getEmail());

        if (email.isPresent()) {
            errors.rejectValue("email", "400",
                    "A user with this email is already registered");
        }

        Optional<String> username = userRepository.findUserByUsername(dto.getUsername());

        if (username.isPresent()) {
            errors.rejectValue("username", "400",
                    "A user with this username is already registered");
        }

        if (errors.hasErrors()) {
            ErrorsUtil.returnErrorsToClient((BindingResult) errors);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterDTO.class.equals(aClass);
    }
}