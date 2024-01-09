package pissir.watermanager.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pissir.watermanager.security.model.ApplicationUser;
import pissir.watermanager.security.model.LoginResponseDTO;
import pissir.watermanager.security.model.Role;
import pissir.watermanager.security.repository.RoleRepository;
import pissir.watermanager.security.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	public ApplicationUser registerUser(String username, String password){
		String encodedPassword = encoder.encode(password);
		Role userRole = roleRepository.findByAuthority("USER").get();
		
		Set<Role> authorities = new HashSet<>();
		
		authorities.add(userRole);
		
		return userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities));
		
	}
	
	
	public LoginResponseDTO loginUser(String username, String password){
		
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);
			
			String token = tokenService.generateJwt(auth);
			
			return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
		} catch (AuthenticationException e){
			return new LoginResponseDTO(null, "");
		}
	}
}
