package com.nexware.aajapan.repositories.custom.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.dto.CustomReportDto;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MCustomListFields;
import com.nexware.aajapan.repositories.custom.TCustomReportRepositoryCustom;
import com.nexware.aajapan.utils.AppUtil;

public class TCustomReportRepositoryCustomImpl implements TCustomReportRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<CustomReportDto> findByCustomField(List<MCustomListFields> customFields, String period, Date from,
			Date to) {
		Date currentDate = new Date();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(currentDate);
		if (period.equalsIgnoreCase("last3months")) {
			cal.add(Calendar.DATE, -90);
			from = AppUtil.atStartOfDay(cal.getTime());
			to = AppUtil.atEndOfDay(currentDate);
		} else if (period.equalsIgnoreCase("thismonth")) {
			from = AppUtil.startDateOfMonth(currentDate);
			to = AppUtil.atEndOfDay(currentDate);
		} else if (period.equalsIgnoreCase("lastmonth")) {
			from = AppUtil.startDateOfMonth(AppUtil.addMonths(currentDate, -1));
			to = AppUtil.endDateOfMonth(from);
		} else if (period.equalsIgnoreCase("period")) {
			from = AppUtil.atStartOfDay(from);
			to = AppUtil.atEndOfDay(to);
		} else if (period.equalsIgnoreCase("alldata")) {

		}

		List<AggregationOperation> aggregationOperations = new ArrayList<>();
		if (!AppUtil.isObjectEmpty(period)) {
			MatchOperation matchOperation = Aggregation.match(Criteria.where("purchaseInfo.date").gte(from).lte(to));
			aggregationOperations.add(matchOperation);
		}

		List<MCustomListFields> parentTableFields = customFields.stream().filter(field -> field.getGroupId() == 0)
				.collect(Collectors.toList());
		List<MCustomListFields> forginKeyTableFields = customFields.stream().filter(field -> field.getGroupId() != 0)
				.collect(Collectors.toList());
		Document fieldProjection = new Document();
		parentTableFields.stream().forEach(f -> fieldProjection.append(f.getProjectAs(), "$" + f.getFieldName()));
		// lookup
		buildLookupAggrigation(forginKeyTableFields, fieldProjection, aggregationOperations);
		// final project
		final AggregationOperation aggregationOperationProjection = context -> new Document("$project",
				fieldProjection);
		aggregationOperations.add(aggregationOperationProjection);

		Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
		final AggregationResults<CustomReportDto> result = mongoTemplate.aggregate(aggregation, "t_stck",
				CustomReportDto.class);
		return result.getMappedResults();

	}

	private void buildLookupAggrigation(List<MCustomListFields> fields, Document fieldProjection,
			List<AggregationOperation> aggregationOperations) {
		Map<Integer, List<MCustomListFields>> lookupMap = fields.stream().collect(
				Collectors.groupingBy(MCustomListFields::getGroupId, Collectors.mapping(f -> f, Collectors.toList())));

		lookupMap.forEach((key, value) -> {
			MCustomListFields field = value.get(0);
			// aggregation
			for (Document doc : field.getAggregation()) {
				String documentString;
				try {
					documentString = objectMapper.writeValueAsString(doc);
					documentString = documentString.replaceAll("_doller_", "\\$");
					Document document = objectMapper.readValue(documentString, Document.class);
					final AggregationOperation aggregationOperation = context -> document;
					aggregationOperations.add(aggregationOperation);
				} catch (Exception e) {
					throw new AAJRuntimeException(e.getMessage());
				}
			}
			value.stream().forEach(f -> fieldProjection.append(f.getProjectAs(),
					"$" + field.getResultToProject() + "." + f.getFieldName()));
		});

	}
}
