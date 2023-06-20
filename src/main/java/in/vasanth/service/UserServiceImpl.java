package in.vasanth.service;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.vasanth.binding.Login;
import in.vasanth.binding.RegistrationPage;
import in.vasanth.entity.Users;
import in.vasanth.repository.UserRepository;
import in.vasanth.utils.Encryption;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;

	@Override
	public boolean registerUser(RegistrationPage form) throws Exception {
		Users findByEmail = userRepo.findByEmail(form.getEmail());
		if (findByEmail != null) {
			return false;
		}
		String input = form.getPassword();
		SecretKey key = Encryption.generateKey(256);
		IvParameterSpec generateIv = Encryption.generateIv();
		String keyStore = Encryption.convertSecretKeyToString(key);
		byte[] iv = generateIv.getIV();
		System.out.println(iv);

		String ivStore = Base64.getEncoder().encodeToString(iv);
		String encrypt = Encryption.encrypt("AES/CFB/NoPadding", input, key, generateIv);
		System.out.println(encrypt);
		Users userDtls = new Users();

		BeanUtils.copyProperties(form, userDtls);
		userDtls.setPassword(encrypt);
		userDtls.setEncryptKey(keyStore);
		userDtls.setInitialVector(ivStore);
		userRepo.save(userDtls);

		return true;

	}

	@Override
	public boolean loginUser(Login lform) throws Exception {

		Users dtls = userRepo.findByEmail(lform.getEmail());
		String password = dtls.getPassword();
		String key = dtls.getEncryptKey();
		SecretKey secret = Encryption.convertStringToSecretKeyto(key);
		String iniVector = dtls.getInitialVector();
		byte[] iniDecode = Base64.getDecoder().decode(iniVector);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iniDecode);
		String decrypt = Encryption.decrypt("AES/CFB/NoPadding", password, secret, ivParameterSpec);
		if (lform.getPassword().equals(decrypt)) {
			session.setAttribute("userId", dtls.getUserId());
			return true;
		}
		return false;
	}

}
