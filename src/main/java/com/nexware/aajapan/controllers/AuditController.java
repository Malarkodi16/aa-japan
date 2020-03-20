package com.nexware.aajapan.controllers;

import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexware.aajapan.models.TPurchaseInvoice;

@RestController
@RequestMapping(value = "/audit")
public class AuditController {

	private final Javers javers;

	@Autowired
	public AuditController(Javers javers) {
		this.javers = javers;
	}

	@RequestMapping("/person")
	public String getPersonChanges() {
		QueryBuilder jqlQuery = QueryBuilder.byClass(TPurchaseInvoice.class);

		List<Change> changes = javers.findChanges(jqlQuery.build());

		return javers.getJsonConverter().toJson(changes);
	}
}