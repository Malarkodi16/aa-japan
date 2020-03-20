package com.nexware.aajapan.controllers.master;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.payload.DatatableResponse;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Controller
@RequestMapping("/master/location")
public class MasterLocationController {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private SequenceService sequenceService;

	@GetMapping("/create")
	public ModelAndView createLocation(@ModelAttribute("locationForm") MLocation locationForm,
			ModelAndView modelAndView) {
		modelAndView.setViewName("shipping.master.location.create");

		return modelAndView;
	}

	@GetMapping("/list")
	public ModelAndView listLocaton(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shipping.master.location.list");

		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView saveLocaton(@ModelAttribute("locationForm") MLocation locationForm,
			RedirectAttributes redirectAttributes, @RequestParam String action) {
		boolean isNewEntry = AppUtil.isObjectEmpty(locationForm.getId());
		MLocation mLocationToSave = null;

		if (isNewEntry) {
			mLocationToSave = locationForm;
			locationForm.setId(null);
			mLocationToSave.setCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MLOC));
			mLocationToSave.setDeleteFlag(Constants.DELETE_FLAG_0);
		} else {
			mLocationToSave = this.locationRepository.findOneById(locationForm.getId());
			mLocationToSave.setType(locationForm.getType());
			mLocationToSave.setAddress(locationForm.getAddress());
			mLocationToSave.setPhone(locationForm.getPhone());
			mLocationToSave.setFax(locationForm.getFax());
			mLocationToSave.setDisplayName(locationForm.getDisplayName());
			mLocationToSave.setShipmentType(locationForm.getShipmentType());
			mLocationToSave.setShipmentOriginPort(locationForm.getShipmentOriginPort());
			mLocationToSave.setAtsukai(locationForm.getAtsukai());
			mLocationToSave.setTantousha(locationForm.getTantousha());
		}
		mLocationToSave.setShipmentOriginCountry("JAPAN");
		this.locationRepository.save(mLocationToSave);
		ModelAndView modelAndView = new ModelAndView();
		if (action.equalsIgnoreCase("save")) {
			modelAndView.setViewName("redirect:/master/location/list");

		} else {
			redirectAttributes.addFlashAttribute("message", "Location saved successfully!");
			modelAndView.setViewName("redirect:/master/location/create");
		}
		return modelAndView;
	}

	@GetMapping("/list-data")
	@ResponseBody
	public DatatableResponse getListData() {
		return new DatatableResponse(this.locationRepository.getListWithoutDelete());
	}

	@GetMapping("/delete/{id}")
	public ModelAndView deleteSupplier(@PathVariable("id") String id, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MLocation location = this.locationRepository.findOneById(id);
		location.setDeleteFlag(Constants.MLOCATION_DELETE_STATUS_AFTER);
		this.locationRepository.save(location);
		redirectAttributes.addFlashAttribute("message", "deleted Successfully");
		modelAndView.setViewName("redirect:/master/location/list");
		return modelAndView;
	}

	@GetMapping("/edit/{code}")
	public ModelAndView locationEdit(@PathVariable("code") String code, ModelAndView modelAndView,
			RedirectAttributes redirectAttributes) {
		MLocation location = this.locationRepository.findOneByCode(code);
		modelAndView.addObject("locationForm", location);
		modelAndView.setViewName("shipping.master.location.create");
		return modelAndView;

	}

	@GetMapping("/check/existing")
	@ResponseBody
	public boolean findExistingLocationList(@RequestParam(value = "id", required = false) String id,
			String displayName) {
		boolean isValid;
		if (id.isEmpty()) {
			isValid = this.locationRepository.existsByDisplayName(displayName);
		} else {
			isValid = this.locationRepository.existsByIdAndDisplayName(id, displayName);
			if (!isValid) {
				isValid = this.locationRepository.existsByDisplayName(displayName);
			} else {
				isValid = false;
			}
		}
		return isValid;

	}
}
