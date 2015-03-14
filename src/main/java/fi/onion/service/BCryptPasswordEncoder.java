package fi.onion.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("passwordEncoder")
public class BCryptPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPass) {
        return BCrypt.hashpw(rawPass.toString(), BCrypt.gensalt());
	}

	@Override
	public boolean matches(CharSequence rawPass, String encPass) {
        return BCrypt.checkpw(rawPass.toString(), encPass);
	}

}
