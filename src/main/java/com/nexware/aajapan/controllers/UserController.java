package com.nexware.aajapan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.MUserDto;
import com.nexware.aajapan.models.MLogin;
import com.nexware.aajapan.models.MUser;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.payload.Response;
import com.nexware.aajapan.repositories.LoginRepository;
import com.nexware.aajapan.repositories.UserRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping(value = { "/create" })
	public ModelAndView createUser(Model model, ModelAndView modelAndView) {

		modelAndView.setViewName("shipping.user");
		return modelAndView;
	}

	@GetMapping("/getRoleSales")
	@ResponseBody
	public List<MLoginDto> findUSerBySales() {

		return loginRepository.findAllByRole();
	}

	@GetMapping("getUser-list")
	@ResponseBody
	public DatatableResponse findAllUser() {
		return new DatatableResponse(userRepository.findAllUsers());
	}

	@PostMapping("create")
	@Transactional
	public ResponseEntity<Response> create(MUserDto userdto, ModelAndView modelAndView) {

		final MUser user = new MUser(userdto.getFullname(), userdto.getDob(), userdto.getMobileno());
		user.setCode(sequenceService.getNextSequence(Constants.SEQUENCE_KEY_USR));
		userRepository.insert(user);
		final MLogin login = new MLogin(userdto.getUsername(), bCryptPasswordEncoder.encode(userdto.getPassword()),
				userdto.getDepartment(), userdto.getRole(), userdto.getReportTo(), user.getCode(),
				userdto.getLocation());
		loginRepository.insert(login);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}

	@PutMapping("edit")
	@Transactional
	public ResponseEntity<Response> edit(MUserDto userdto, @RequestParam("userId") String userId,
			ModelAndView modelAndView) {
		final MUser user = userRepository.findOneByCode(userId);
		if (!AppUtil.isObjectEmpty(user)) {
			final MLogin login = loginRepository.findOneByUserId(user.getCode());
			user.setFullname(userdto.getFullname());
			user.setDob(userdto.getDob());
			user.setMobileno(userdto.getMobileno());
			login.setDepartment(userdto.getDepartment());
			login.setRole(userdto.getRole());
			login.setReportTo(userdto.getReportTo());
			login.setLocation(userdto.getLocation());
			login.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
			userRepository.save(user);
			loginRepository.save(login);
		}
//		final MLogin login = new MLogin(userdto.getUsername(), bCryptPasswordEncoder.encode(userdto.getPassword()),
//				userdto.getDepartment(), userdto.getRole(), userdto.getReportTo(), user.getCode(),
//				userdto.getLocation());
//		loginRepository.insert(login);
		return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
	}
}
