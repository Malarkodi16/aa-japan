var countriesJson, country, port, transportersJson, shippingInstructionId, orginCountryList;
var tableRequestFromSales;
var table_shipping_requested
$(function() {

	$(document).on('focus', 'input,select,textarea,.select2', function() {
		$(this).closest('.element-wrapper').addClass('highlight');
	});
	$(document).on('blur', 'input,select,textarea,.select2', function() {
		$(this).closest('.element-wrapper').removeClass('highlight');
	})
	shippingInstructionId = $('#request-from-sales-filter-id').val()
	$(document).on('focus', '.select2-selection--single', function(e) {
		select2_open = $(this).parent().parent().siblings('select');
		select2_open.select2('open');
	});

	//set status
    setShippingDashboardStatus();
	// Date picker
	$(this).find('.datepicker').datepicker({
		format : "dd-mm-yyyy",
		autoclose : true
	})
	$('#table-filter-request-from-sales-type').select2({
		allowClear : true,
		width : '100%'
	});
	//AutoNumeric
	$('input[name="charge"]').autoNumeric('init');
	//icheck
	$(
			'input[type="checkbox"][name="btnIsCustomer"].minimal, input[type="radio"][name="btnIsCustomer"].minimal,input[type="checkbox"][name="lastLap"]')
			.iCheck({
				checkboxClass : 'icheckbox_minimal-blue',
				radioClass : 'iradio_minimal-blue'
			})
	var shipmentTypeFilter = $(
			'input[type="radio"][name="shippingType"]:checked').val();
	$('input[type="radio"][name="shippingType"].minimal').iCheck({
		checkboxClass : 'icheckbox_minimal-blue',
		radioClass : 'iradio_minimal-blue'
	}).on('ifChecked', function() {
		shipmentTypeFilter = $(this).val();
		if (shipmentTypeFilter == 1) {
			$('#btn-roro-shipping').removeClass('hidden');
			$('#btn-container-shipping').addClass('hidden');
		} else if (shipmentTypeFilter == 2) {
			$('#btn-roro-shipping').addClass('hidden');
			$('#btn-container-shipping').removeClass('hidden');
		}
		tableRequestFromSales.draw();
	})
	$('input[type="radio"][name=radioShowTable].minimal')
			.iCheck({
				checkboxClass : 'icheckbox_minimal-blue',
				radioClass : 'iradio_minimal-blue'
			})
			.on(
					'ifChecked',
					function(e) {
						if ($(this).val() == 0) {
							$(
									'#shipping-requested,#request-from-sales-container,#shipping-container,#container-shipment-filter,#roro-shipment-filter')
									.addClass('hidden')
							$('#available-for-shipping').removeClass('hidden')
							tableAvailableForShipping.ajax.reload()
						} else if ($(this).val() == 1) {
							$(
									'#shipping-requested,#available-for-shipping,#shipping-container,#container-shipment-filter,#roro-shipment-filter')
									.addClass('hidden')
							$('#request-from-sales-container').removeClass(
									'hidden')
							if (typeof tableRequestFromSales != 'undefined') {
								tableRequestFromSales.ajax.reload()
							}
						} else if ($(this).val() == 2) {
							$(
									'#available-for-shipping,#request-from-sales-container,#shipping-requested,#roro-shipment-filter')
									.addClass('hidden')
							$('#shipping-container,#container-shipment-filter')
									.removeClass('hidden')
							table_shipping_container.ajax.reload()
						} else if ($(this).val() == 3) {
							$(
									'#available-for-shipping,#request-from-sales-container,#shipping-container,#container-shipment-filter')
									.addClass('hidden')
							$('#shipping-requested,#roro-shipment-filter')
									.removeClass('hidden')
							table_shipping_requested.ajax.reload()
						}
					})
	$('input[type="radio"][name="containerAllcationStatus"].minimal').iCheck({
		checkboxClass : 'icheckbox_minimal-blue',
		radioClass : 'iradio_minimal-blue'
	}).on('ifChecked', function(e) {
		table_shipping_container.ajax.reload()
	});
	$('input[type="radio"][name="roroAllcationStatus"].minimal').iCheck({
		checkboxClass : 'icheckbox_minimal-blue',
		radioClass : 'iradio_minimal-blue'
	}).on('ifChecked', function(e) {
		table_shipping_requested.ajax.reload()
	});
	if (!isEmpty(shippingInstructionId)) {
		$('input[type="radio"][name=radioShowTable][value="1"].minimal')
				.iCheck('check').trigger('ifChecked');
	}

	//inspection module start
	$('#arrange-inspection-modal').on('show.bs.modal', function(e) {
		if (tableAvailableForShipping.rows({
			selected : true,
			page : 'current'
		}).count() == 0) {
			alert($.i18n.prop('common.alert.stock.noselection'));
			return e.preventDefault();
		}
	}).on('hidden.bs.modal', function() {
		resetElementInput(this)
	}).on(
			'click',
			'#inspection-submit',
			function() {
				if (!$('#form-arrange-inspection').valid()) {
					return;
				}
				var data_stock = [];
				tableAvailableForShipping.rows({
					selected : true,
					page : 'current'
				}).every(function(rowIdx, tableLoop, rowLoop) {
					var data = tableAvailableForShipping.row(this).data();
					data_stock.push(data.stockNo);
				})
				var arrangeInspectionData = $('#arrange-inspection-modal');
				var object = getFormData(arrangeInspectionData
						.find('input,select,textarea'));
				var inspectionOrderData = [];
				for (var i = 0; i < data_stock.length; i++) {
					data = {};
					data.stockNo = data_stock[i];
					data.country = object.country;
					data.forwarder = object.forwarder;
					data.comment = object.comment;
					inspectionOrderData.push(data);
				}
				$.ajax({
					beforeSend : function() {
						$('#spinner').show()
					},
					complete : function() {
						$('#spinner').hide();
					},
					type : "post",
					data : JSON.stringify(inspectionOrderData),
					url : myContextPath + "/inspection/save",
					contentType : "application/json",
					success : function(status) {
						tableAvailableForShipping.ajax.reload();
						$('#arrange-inspection-modal').modal('toggle');

					}
				})

			});

	//inspection module end

	// transport module start
	var radioSchedule = $('#modal-arrange-transport')
			.find(
					'input[type="radio"][name="selectedtype"].minimal,input[type="radio"][name="selecteddate"].minimal');
	radioSchedule.iCheck({
		checkboxClass : 'icheckbox_minimal-blue',
		radioClass : 'iradio_minimal-blue'
	})

	$('#item-vehicle-container').slimScroll({
		start : 'bottom',
		height : ''
	});
	$('#btn-create-transport-order').on(
			'click',
			function() {
				if (!$('#transport-arrangement-form').valid()) {
					return;
				}

				var objectArr = [];
				var data = {};
				autoNumericSetRawValue($('#modal-arrange-transport').find(
						'.charge'))
				var scheduleDetails = getFormData($(
						'#transport-schedule-details').find('.schedule-data'));
				var selectedType = getFormData($('#transport-schedule-details')
						.find('.selected-type'));
				var transportComment = getFormData($('#transport-comment')
						.find('.comment'));
				$("#item-vehicle-container").find('.item-vehicle').each(
						function() {
							var object = {};
							object = getFormData($(this).find(
									'input,select,textarea'));
							object.pickupDate = scheduleDetails.pickupDate;
							object.pickupTime = scheduleDetails.pickupTime;
							object.deliveryDate = scheduleDetails.deliveryDate;
							object.scheduleType = selectedType.selectedtype;
							object.deliveryTime = scheduleDetails.deliveryTime;
							object.selectedDate = scheduleDetails.selecteddate;
							object.comment = transportComment.comment;
							objectArr.push(object);
						});

				$.ajax({
					beforeSend : function() {
						$('#spinner').show()
					},
					complete : function() {
						$('#spinner').hide();
					},
					type : "post",
					data : JSON.stringify(objectArr),
					url : myContextPath + "/transport/order/save",
					contentType : "application/json",
					success : function(data) {
						if (data.status === 'success') {

							tableAvailableForShipping.ajax.reload();
							$('#modal-arrange-transport').modal('toggle');
						}
					}
				});
			})

	//transport arrangement dropdown options
	$.getJSON(myContextPath + "/data/locations.json", function(data) {
		locationJson = data;
		$('#transport-items').find(
				'select[name="pickupLocation"],select[name="dropLocation"]')
				.select2({

					allowClear : true,
					width : '100%',
					data : $.map(locationJson, function(item) {
						return {
							id : item.code,
							text : item.displayName
						};
					})
				});
		var newOption = new Option("Others", "others", false, false);
		// Append it to the select
		$('#transport-items').find(
				'select[name="pickupLocation"],select[name="dropLocation"]')
				.append(newOption);
		$('#item-vechicle-clone').find(
				'select[name="pickupLocation"],select[name="dropLocation"]')
				.select2('destroy')
	})
	$
			.getJSON(
					myContextPath + "/data/forwarders.json",
					function(data) {
						forwardersJson = data;
						$(
								'#transport-items,#modal-arrange-shipping,#modal-container-allocation,#arrange-inspection-modal')
								.find('select[name="forwarder"],#forwarderCode')
								.select2(
										{
											placeholder : "Select forwarder",
											allowClear : true,
											width : '100%',
											data : $.map(forwardersJson,
													function(item) {
														return {
															id : item.code,
															text : item.name
														};
													})
										}).val('').trigger('change');
						//         $('#arrange-inspection-modal').find('#forwarderCode').select2({

						//             allowClear: true,
						//             width: '100%',
						//             data: $.map(forwardersJson, function(item) {
						//                 return {
						//                     id: item.code,
						//                     text: item.name
						//                 };
						//             })
						//         }).val('').trigger('change');
						$('#item-vechicle-clone').find(
								'select[name="forwarder"]').select2('destroy')
					})
	$.getJSON(myContextPath + "/data/categories.json", function(data) {
		categoriesJson = data;
		$('#transport-items').find('select[name="category"]').select2({
			allowClear : true,
			width : '100%',
			data : $.map(categoriesJson, function(item) {
				var childrenArr = [];
				$.each(item.subCategories, function(i, val) {
					childrenArr.push({
						id : val.name,
						text : val.name
					})
				})
				return {
					text : item.name,
					children : childrenArr
				};
			})
		})

		$('#item-vechicle-clone').find('select[name="category"]').select2(
				'destroy')
	})
	$.getJSON(myContextPath + "/data/transporters.json", function(data) {
		transportersJson = data;
		$('#modal-arrange-transport').find('.transporter').select2({
			placeholder : "Select Transporter",
			allowClear : true,
			width : '100%',
			data : $.map(transportersJson, function(item) {
				return {
					id : item.code,
					text : item.name
				};
			})
		}).val('').trigger('change');
		$('#item-vechicle-clone').find('select[name="transporter"]').select2(
				'destroy');
	})
	// transport module end

	$.getJSON(myContextPath + "/data/forwarders.json", function(data) {

		$('select[name="requested-forwarder-filter"]').select2({
			allowClear : true,
			width : '100%',
			data : $.map(data, function(item) {
				return {
					id : item.code,
					text : item.name
				};
			})
		});
	})
	$.getJSON(myContextPath + "/japan/find-port", function(data) {
		let japanPortsJson = data;
		var elements = $('.japanPorts');
		elements.select2({
			allowClear : true,
			width : '100%',
			data : $.map(japanPortsJson.port, function(item) {
				return {
					id : item,
					text : item
				};
			})
		}).val('').trigger('change');
	})

	//Shipping Requested
	$.getJSON(myContextPath + "/shipping/data/shipsWithCompanyName", function(
			data) {
		shippingJson = data.data;
		var elements = $('#shipping-company-filter');
		$('#shipping-company-filter').select2({
			allowClear : true,
			width : '100%',
			data : $.map(shippingJson, function(item) {
				return {
					id : item.shippingCompanyNo,
					text : item.shippingCompanyName,
					data : item
				};
			})
		}).val('').trigger('change');
		$('#cloneable-items').find('#shipping-company-filter').select2(
				'destroy');
	})

	var shippingRequestedShipFilterELe = $('#ship-filter');
	shippingRequestedShipFilterELe.select2({
		allowClear : true,
		width : '100%',
	}).on('change', function() {
		port = $(this).val();
		//table_shipping_requested.draw();
	})

	$('#shipping-company-filter').on('change', function() {
		shippingRequestedShipFilterELe.empty()
		var data = $(this).select2('data');
		if (typeof data[0].data == 'undefined') {
			return;
		}

		$('#ship-filter').select2({
			allowClear : true,
			width : '100%',
			data : $.map(data[0].data.items, function(item) {
				return {
					id : item.shipId,
					text : item.name

				};
			})
		}).val('').trigger('change');
	})
	var requestFromSalesPortFilterELe = $('#port-filter-from-sales');
	requestFromSalesPortFilterELe.select2({
		allowClear : true,
		width : '100%',
	}).on('change', function() {
		port = $(this).val();
		tableRequestFromSales.draw();
	})

	$('#country-filter-from-sales').on(
			'change',
			function() {
				var val = ifNotValid($(this).val(), '');
				requestFromSalesPortFilterELe.empty();
				if (!isEmpty(val)) {
					var data = filterOneFromListByKeyAndValue(countriesJson,
							"country", val);
					if (data != null) {
						requestFromSalesPortFilterELe.select2({
							allowClear : true,
							width : '100%',
							data : $.map(data.port, function(item) {
								return {
									id : item,
									text : item
								};
							})
						}).val('').trigger('change');
					}

				}
				country = val;
				tableRequestFromSales.draw();
			});

	//stock details modal update
	var stockDetailsModal = $('#modal-stock-details');
	var stockDetailsModalBody = stockDetailsModal
			.find('#modal-stock-details-body');
	var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
	var stockCloneElement = $('#stock-details-html>.stock-details');
	stockDetailsModalBodyDiv.slimScroll({
		start : 'bottom',
		height : ''
	});
	stockDetailsModal.on('show.bs.modal', function(e) {
		if (e.namespace != 'bs.modal') {
			return;
		}
		var targetElement = $(e.relatedTarget);
		var stockNo = targetElement.attr('data-stockNo');
		stockCloneElement.clone().appendTo(stockDetailsModalBody);
		//updateStockDetailsData
		updateStockDetailsData(stockDetailsModal, stockNo)
	}).on('hidden.bs.modal', function() {
		stockDetailsModalBody.html('');
	})

	//table request from sales
	var tableEle = $('#table-request-from-sales');
	tableRequestFromSales = tableEle
			.DataTable({
				"dom" : '<ip<t>ip>',
				"pageLength" : 25,
				"ajax" : {
					"url" : myContextPath
							+ "/shipping/instruction/from-sales/datasource",
					"data" : function(data) {
						data["draw"] = $('#request-from-sales-container')
								.hasClass('hidden') ? false : true
						return data;
					}
				},
				ordering : false,

				select : {
					style : 'multi',
					selector : 'td:first-child>input'
				},
				columnDefs : [
						{
							"targets" : '_all',
							"defaultContent" : ""
						},
						{
							targets : 0,
							orderable : false,
							className : 'select-checkbox',
							"data" : "id",
							"render" : function(data, type, row) {
								data = data == null ? '' : data;
								if (type === 'display') {
									return '<input class="selectBox" type="checkbox" value="'
											+ data + '">'
								}
								return data;
							}
						},
						{
							targets : 1,
							"className" : "details-control",
							"data" : "stockNo",
							"render" : function(data, type, row) {
								data = data == null ? '' : data;
								if (type === 'display') {

									return '<input type="hidden" name="stockNo" value="'
											+ data
											+ '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="'
											+ data + '">' + data + '</a>'
								}
								return data;
							}
						},
						{
							targets : 2,
							"className" : "details-control",
							"data" : "chassisNo"
						},
						{
							targets : 3,
							"className" : "details-control",
							"data" : "fCustName"
						},
						{
							targets : 4,
							"className" : "details-control",
							"data" : "fConsigneeName"
						},
						{
							targets : 5,
							"className" : "details-control",
							"data" : "fNotifyPartyName"
						},
						{
							targets : 6,

							"data" : "destinationCountry",
							"className" : 'align-center',
							"render" : function(data, type, row) {
								return row.destinationCountry + '/'
										+ row.destinationPort;
							}

						},
						{
							targets : 7,
							"data" : "shipmentType",
							"className" : 'align-center',
							"render" : function(data, type, row) {
								if (ifNotValid(data, -1) == 1) {
									return 'RORO'
								} else if (ifNotValid(data, -1) == 2) {
									return 'CONTAINER'
								}
								return '';
							}

						},
						{
							targets : 8,
							"data" : "salesPersonId",
							"className" : 'align-center'
						},
						{
							targets : 9,
							"data" : "destinationCountry",
							"className" : 'align-center',
							"visible" : false
						},
						{
							targets : 10,
							"data" : "destinationPort",
							"className" : 'details-control',
							"visible" : false
						},
						{
							targets : 11,
							"data" : "estimatedDeparture",
							"className" : 'details-control'
						},
						{
							targets : 12,
							"data" : "estimatedArrival",
							"className" : 'details-control'
						},
						{
							targets : 13,
							"className" : 'details-control',
							"data" : 'lastTransportLocation',
							"render" : function(data, type, row) {

								return ifNotValid(data, '').toUpperCase() == 'OTHERS' ? ifNotValid(
										row.lastTransportLocationCustom, '')
										: ifNotValid(
												row.lastTransportLocationDisplayname,
												'');
								;
							}
						},
						{
							targets : 14,
							"data" : "remarks",
							"className" : 'details-control'
						},
						{
							targets : 15,
							"className" : 'details-control',
							"render" : function(data, type, row) {
								return '<button type="button" class="btn btn-warning btn-xs" data-target="#modal-update-remark" data-toggle="modal" data-backdrop="static" style=" width: 120px; "><i class="fa fa-fw fa-floppy-o"></i> Update Remark</button>';
							}
						}, {
							targets : 16,
							"data" : "shipmentType",
							"visible" : false
						}, {
							targets : 17,
							"data" : "forwarder",
							"visible" : false
						} ]

			});
	$('select[name="requested-forwarder-filter"]').change(function() {
		tableRequestFromSales.draw();
	})
	//<!-- ./. added by krishna -->
	// Date range picker
	var departure_date_min;
	var departure_date_max;
	$('#table-filter-request-from-sales-departure-date').daterangepicker({
		autoUpdateInput : false
	}).on(
			"apply.daterangepicker",
			function(e, picker) {
				departure_date_min = picker.startDate;
				departure_date_max = picker.endDate;
				picker.element.val(departure_date_min.format('DD-MM-YYYY')
						+ ' - ' + departure_date_max.format('DD-MM-YYYY'));
				departure_date_min = departure_date_min._d.getTime();
				departure_date_max = departure_date_max._d.getTime();
				$(this).closest('.input-group').find('.clear-date-departure')
						.remove();
				$('<div>', {
					'class' : 'input-group-addon clear-date-departure'
				}).append($('<i>', {
					'class' : 'fa fa-times'
				})).appendTo($(this).closest('.input-group'))
				tableRequestFromSales.draw();
			});
	$('#date-form-group').on('click', '.clear-date-departure', function() {
		departure_date_min = '';
		departure_date_max = '';
		tableRequestFromSales.draw();
		$('#table-filter-request-from-sales-departure-date').val('');
		$(this).remove();

	})

	var arrival_date_min;
	var arrival_date_max;
	$('#table-filter-request-from-sales-arrival-date').daterangepicker({
		autoUpdateInput : false
	}).on(
			"apply.daterangepicker",
			function(e, picker) {
				arrival_date_min = picker.startDate;
				arrival_date_max = picker.endDate;
				picker.element.val(arrival_date_min.format('DD-MM-YYYY')
						+ ' - ' + arrival_date_max.format('DD-MM-YYYY'));
				arrival_date_min = arrival_date_min._d.getTime();
				arrival_date_max = arrival_date_max._d.getTime();
				$(this).closest('.input-group').find('.clear-date-arrival')
						.remove();
				$('<div>', {
					'class' : 'input-group-addon clear-date-arrival'
				}).append($('<i>', {
					'class' : 'fa fa-times'
				})).appendTo($(this).closest('.input-group'))
				tableRequestFromSales.draw();
			});
	$('#date-form-group-arrival').on('click', '.clear-date-arrival',
			function() {
				arrival_date_min = '';
				arrival_date_max = '';
				tableRequestFromSales.draw();
				$('#table-filter-request-from-sales-arrival-date').val('');
				$(this).remove();

			})

	//on open recycle modal
	var modalUpdateRemarkTriggerEle;
	var modalUpdateRemark = $('#modal-update-remark');
	modalUpdateRemark.on(
			'show.bs.modal',
			function(event) {
				if (event.namespace != 'bs.modal') {
					return;
				}
				modalUpdateRemarkTriggerEle = $(event.relatedTarget);
				var data = tableRequestFromSales.row(
						$(modalUpdateRemarkTriggerEle).closest('tr')).data();
				modalUpdateRemark.find('#rowData').attr('data-json',
						JSON.stringify(data));
			}).on('hidden.bs.modal', function() {
		resetElementInput($(this));
	}).on(
			'click',
			'#save-remark-modal',
			function() {
				var row = tableRequestFromSales.row($(
						modalUpdateRemarkTriggerEle).closest('tr'));
				var rowData = modalUpdateRemark.find('#rowData').attr('data-json');
				rowData=JSON.parse(rowData);
				var data = {};
				var id = rowData.id;
				data["remarks"] = modalUpdateRemark.find(
						'textarea[name="remarks"]').val();
				var response = updateRemarks(id, data)
				//updateClaimReceivedAmount
				if (response.status === 'success') {
					if (!isEmpty(response.data)) {
						row.data(response.data).invalidate();
						tableRequestFromSales.draw();
					}
					modalUpdateRemark.modal('toggle');
				}
			})
	//<!-- ./. added by krishna -->

	tableRequestFromSales.on(
			"click",
			"th.select-checkbox>input",
			function() {
				if (!$(this).is(':checked')) {
					tableRequestFromSales.rows({
						page : 'current'
					}).deselect();
					$("th.select-checkbox").removeClass("selected");
					tableRequestFromSales.rows({
						page : 'current'
					}).every(
							function(rowIdx, tableLoop, rowLoop) {
								$(this.node()).find(
										'td:first>input[class="selectBox"]')
										.prop('checked', false);

							});
				} else {
					tableRequestFromSales.rows({
						page : 'current'
					}).select();
					$("th.select-checkbox").addClass("selected");
					tableRequestFromSales.rows({
						page : 'current'
					}).every(
							function(rowIdx, tableLoop, rowLoop) {
								$(this.node()).find(
										'td:first>input[class="selectBox"]')
										.prop('checked', true);

							});
				}
			}).on(
			"select",
			function() {
				if (tableRequestFromSales.rows({
					selected : true,
					page : 'current'
				}).count() !== tableRequestFromSales.rows({
					page : 'current',
					search : 'applied'
				}).count()) {
					$(tableEle).find("th.select-checkbox>input").removeClass(
							"selected");
					$(tableEle).find("th.select-checkbox>input").prop(
							'checked', false);
				} else {
					$(tableEle).find("th.select-checkbox>input").addClass(
							"selected");
					$(tableEle).find("th.select-checkbox>input").prop(
							'checked', true);

				}

			}).on(
			"deselect",
			function() {
				if (tableRequestFromSales.rows({
					selected : true,
					page : 'current'
				}).count() !== tableRequestFromSales.rows({
					page : 'current'
				}).count()) {
					$(tableEle).find("th.select-checkbox>input").removeClass(
							"selected");
					$(tableEle).find("th.select-checkbox>input").prop(
							'checked', false);
				} else {
					$(tableEle).find("th.select-checkbox>input").addClass(
							"selected");
					$(tableEle).find("th.select-checkbox>input").prop(
							'checked', true);

				}

			});
	// Customize Datatable
	$('#table-request-from-sales-filter-search').keyup(function() {
		tableRequestFromSales.search($(this).val()).draw();
	});
	$('#table-request-from-sales-filter-length').change(function() {
		tableRequestFromSales.page.len($(this).val()).draw();
	});

	//table shipping requested
	var table_shipping_requested_Ele = $('#table-shipping-requested');
	table_shipping_requested = table_shipping_requested_Ele
			.DataTable({
				"dom" : '<ip<t>ip>',
				"pageLength" : 25,
				"ajax" : {
					"url" : myContextPath + "/shipping/requested/datasource",
					"data" : function(data) {
						data["draw"] = $('#shipping-requested').hasClass(
								'hidden') ? false : true
						data["draw"] = $('#table-shipping-requested').hasClass(
								'hidden') ? false : true
						data["show"] = $(
								'#roro-shipment-filter input[name="roroAllcationStatus"]:checked')
								.val();
						return data;
					}
				},

				columnDefs : [
						{
							"targets" : '_all',
							"defaultContent" : ""
						},
						{
							targets : 0,
							orderable : false,
							className : 'details-control',
							"data" : "vessel",
							"render" : function(data, type, row) {
								data = data == null ? '' : data;
								if (type === 'display') {
									var html = '';
									var actionHtml = '';
									html += '<a href="'
											+ myContextPath
											+ '/shipping/order/request?shipId='
											+ row.shipId
											+ '&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Order request [PDF]</a>'
									if (row.status == 1) {
										actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-flag="roro" data-keyboard="false" data-toggle="modal" data-target="#modal-update-vessel-confirmed"><i class="fa fa-fw fa-pencil"></i>Update BL</a>';
									}
									return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">'
											+ row.shippingCompanyName
											+ ' - '
											+ row.vessel
											+ ' ['
											+ row.voyageNo
											+ ']</span></h5></div><div class="action-container pull-right hidden">'
											+ html
											+ '</div><div class="action-container pull-right">'
											+ actionHtml
											+ '</div></div></div></div>';
								}

								return data;
							}
						}, {
							targets : 1,
							"className" : "details-control",
							"data" : "shippingCompanyNo",
							"visible" : false
						}, {
							targets : 2,
							"className" : "details-control",
							"data" : "shipId",
							"visible" : false
						}, {
							targets : 3,
							"className" : "details-control",
							"data" : "voyageNo",
							"visible" : false
						} ],

				"fnDrawCallback" : function(oSettings) {
					$(oSettings.nTHead).hide();
				}

			});
	// Customize Datatable
	$('#table-shipping-requested-filter-search').keyup(function() {
		table_shipping_requested.search($(this).val()).draw();
	});
	$('#table-shipping-requested-filter-length').change(function() {
		table_shipping_requested.page.len($(this).val()).draw();
	});
	//expand details
	table_shipping_requested.on('click', 'td.details-control', function() {
		var tr = $(this).closest('tr');
		var row = table_shipping_requested.row(tr);
		if (row.child.isShown()) {
			row.child.hide();
			tr.removeClass('shown');
			tr.find('i[name="icon"]').removeClass('fa-minus-square-o')
					.addClass('fa-plus-square-o');
		} else {
			table_shipping_requested.rows('.shown').every(
					function(rowIdx, tableLoop, rowLoop) {
						var row = table_shipping_requested.row(rowIdx);
						if (row.child.isShown()) {
							row.child.hide();
							tr.removeClass('shown');
						}

					})
			row.child(format(row.data())).show();
			tr.addClass('shown');
			tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass(
					'fa-minus-square-o');
		}
	});
	table_shipping_requested.on('click', 'a.cancel-shipping', function() {
		if (confirm($.i18n.prop('confirm.shipping.request.cancel'))) {
			var reqData = {};
			var tr = $(this).closest('tr');
			var detailsContainer = tr.closest('.detail-view')
			var data = tr.attr('data-json');
			data=JSON.parse(data);
			reqData["id"] = data.shipmentRequestId;
			reqData["scheduleId"] = data.scheduleId;
			var result = cancelShippingRequest(reqData);
			if (result.status == 'success') {
				var parentRow = detailsContainer.closest('tr').prev('tr');
				var row = table_shipping_requested.row(parentRow);
				if (result.data != null) {
					row.data(result.data).invalidate();
					tr.remove()
				} else {
					row.remove().draw();
				}

			}
		}
	})
	//     modal scroll 
	$('#modal-arrange-shipping').find('#item-container').slimScroll({
		start : 'bottom',
		height : ''
	});

	//on show accept shipping
	var modalShippingAcceptBtn;
	$('#accept-shipping-request-modal')
			.on('show.bs.modal', function(event) {
				if (event.namespace != 'bs.modal') {
					return;
				}
				modalShippingAcceptBtn = $(event.relatedTarget);
				var data = modalShippingAcceptBtn.closest('tr').attr('data-json');
				data=JSON.parse(data);

				//         $(this).find('#shipId').val(modalShippingAcceptBtn.closest('.detail-view').find('input[name="shipId"]').val());
				$(this).find('#scheduleId').val(data.scheduleId);
				$(this).find('#shipmentRequestId').val(data.shipmentRequestId);
			})
			.on('hidden.bs.modal', function() {
				resetElementInput(this);
			})
			.on(
					'click',
					'#btn-save',
					function() {
						var modal = $('#accept-shipping-request-modal');
						var dhlNo = ifNotValid(modal
								.find('input[name="dhlNo"]').val(), '');
						var shipmentRequestId = ifNotValid(modal.find(
								'#shipmentRequestId').val(), '');
						//         var shipId = ifNotValid(modal.find('#shipId').val(), '');
						var scheduleId = ifNotValid(modal.find('#scheduleId')
								.val(), '');
						var data = {};
						//WARNING field name should be matched with entity name        
						data['dhlNo'] = dhlNo;
						var tr = modalShippingAcceptBtn.closest('tr');
						var detailsContainer = tr.closest('.detail-view')
						var result = saveShippingRequestAcceptDetails(data,
								shipmentRequestId, scheduleId);
						if (result.status == 'success') {
							var parentRow = detailsContainer.closest('tr')
									.prev('tr');
							var row = table_shipping_requested.row(parentRow);
							if (result.data != null) {
								row.data(result.data).invalidate();
								tr.remove()
							} else {
								row.remove().draw();
							}

							$('#accept-shipping-request-modal').modal('toggle');
						}

					})

	//arrange shipping for sales and rearrange
	$('#modal-arrange-shipping')
			.on(
					'show.bs.modal',
					function(event) {
						if (event.namespace != 'bs.modal') {
							return;
						}
						var triggerElement = $(event.relatedTarget);
						let forwarder = $(
								'select[name="requested-forwarder-filter"]')
								.val();
						if (isEmpty(forwarder)) {
							alert($.i18n
									.prop('shipping.request.validate.forwarder'));
							return event.preventDefault();
						}
						if (tableRequestFromSales.rows({
							selected : true,
							page : 'current'
						}).count() == 0) {
							alert($.i18n.prop('common.alert.stock.noselection'));
							return event.preventDefault();
						}
						let dataArray = tableRequestFromSales.rows({
							selected : true,
							page : 'current'
						}).data().toArray();
						//         $(this).find('select[name="orginCountry"]').val('JAPAN').trigger('change');
						//         $(this).find('select[name="orginPort"]').val(dataArray[0].originPort).trigger('change')
						$(this).find('select[name="destCountrySelect"]').val(
								dataArray[0].destinationCountry).trigger(
								'change');
						$(this).find('select[name="destPortSelect"]').val(
								dataArray[0].destinationPort).trigger('change');
						$(this).find('input[name="destCountry"]').val(
								dataArray[0].destinationCountry);
						$(this).find('input[name="destPort"]').val(
								dataArray[0].destinationPort);
						$(this).find('select[name="yardSelect"]').val(
								dataArray[0].yard).trigger('change');
						$(this).find('select[name="forwarder"]').val(forwarder)
								.trigger('change');
						$(this).find('input[name="yard"]').val(
								dataArray[0].yard);
						$(this).find('select[name="shippingTypeSelect"]').val(
								dataArray[0].shipmentType).trigger('change')
								.addClass('readonly');
						$(this).find('input[name="shippingType"]').val(
								dataArray[0].shipmentType);

						if (dataArray[0].destCountry == 'Kenya') {
							$(this).find('#yardFields').removeClass('hidden');
						} else {
							$(this).find('#yardFields').addClass('hidden');
						}

						var tableElement = $(this).find(
								'table#stock-table-details>tbody');
						var rowClone = tableElement.find('tr.clone-row');

						for (var i = 0; i < dataArray.length; i++) {
							var row = $(rowClone).clone();
							$(row).find('td.s-no>span').html(i + 1)
							$(row).find('td.s-no>input').val(dataArray[i].id);
							$(row).find('td.stockNo')
									.html(dataArray[i].stockNo);
							$(row).find('td.chassisNo').html(
									dataArray[i].chassisNo);
							$(row).find('td.maker').html(dataArray[i].maker);
							$(row).find('td.model').html(dataArray[i].model);
							$(row).find('td.m3').html(dataArray[i].m3);
							$(row).find('td.length').html(dataArray[i].length);
							$(row).find('td.width').html(dataArray[i].width);
							$(row).find('td.height').html(dataArray[i].height);
							$(row).removeClass('hide');
							tableElement.append(row);
						}

						$(this).find('select[name="shippingTypeSelect"]')
								.select2({
									allowClear : true,
									width : '100%',
									"readonly" : true
								});

					})
			.on(
					'hidden.bs.modal',
					function() {
						$(this).find('#item-container>.clone-item').remove();
						$('#stock-table-details>tbody>.clone-row')
								.not(':first').remove();
					})
			.on(
					'change',
					'select[name="destCountrySelect"]',
					function() {
						var element = $(this).closest('.item');
						var country = ifNotValid($(this).val(), '');
						if (country.toUpperCase() == 'KENYA') {
							$('#modal-arrange-shipping #yardFields')
									.removeClass('hidden');
						} else {
							$('#modal-arrange-shipping #yardFields').addClass(
									'hidden');
						}
						$(element).find('input[name="destCountry"]').val(
								country);
						var vessalElement = $(element).find(
								'select[name="scheduleId"]');

						element.find('.schedule-container').addClass('hidden');
						vessalElement.empty();
						var portElement = $(element).find(
								'select[name="destPortSelect"]');
						var yardElement = $(element).find(
								'select[name="yardSelect"]');
						portElement.empty();
						yardElement.empty();
						if (country.length == 0) {
							return;
						}
						var data = filterOneFromListByKeyAndValue(
								countriesJson, "country", country);
						if (data != null) {
							portElement.select2({
								allowClear : true,
								width : '100%',
								data : $.map(data.port, function(item) {
									return {
										id : item,
										text : item
									};
								})
							}).val('').trigger('change');
							yardElement.select2({
								allowClear : true,
								width : '100%',
								data : $.map(data.yardDetails, function(item) {
									return {
										id : item.id,
										text : item.yardName
									};
								})
							}).val('').trigger('change');
						}

					})
			.on(
					'change',
					'select[name="destPortSelect"]',
					function() {
						var destPort = ifNotValid($(this).val(), '');
						var element = $(this).closest('.item');
						$(element).find('input[name="destPort"]').val(destPort);
						var vessalElement = $(element).find(
								'select[name="scheduleId"]');
						//         var forwarderElement = $(element).find('select[name="forwarder"]');

						element.find('input[name="voyageNo"]').val('');
						element.find('input[name="scheduleId"]').val('');
						element.find('.schedule-container').addClass('hidden');
						vessalElement.empty();
						//         forwarderElement.empty();
						var data = {};

						data["destCountry"] = ifNotValid($(element).find(
								'input[name="destCountry"]').val(), '');
						data["destPort"] = destPort;
						if (!isEmpty(data.destCountry)
								&& !isEmpty(data.destPort)) {
							var response = findAllVessalsAndFwdrByOrginAndDestination(data);
							response = ifNotValid(response.data, {});
							var vessalArr = response.vessals;
							//             var forwardersArr = response.forwarders;
							//init vessal dropdown
							vessalElement
									.select2(
											{
												allowClear : true,
												width : '100%',
												data : $
														.map(
																vessalArr,
																function(item) {
																	return {
																		id : item.scheduleId,
																		text : item.shipName
																				+ ' ['
																				+ item.shippingCompanyName
																				+ ']',
																		data : item
																	};
																})
											}).val('').trigger('change');
							//init forwarder dropdown
							//             forwarderElement.select2({
							//                 allowClear: true,
							//                 width: '100%',
							//                 data: $.map(forwardersArr, function(item) {
							//                     return {
							//                         id: item.forwarderId,
							//                         text: item.forwarderName,
							//                         data: item
							//                     };
							//                 })
							//             }).val('').trigger('change');
							//             $(vessalElement).attr('data-json', JSON.stringify(vessalArr));
						}

					})
			.on(
					'change',
					'select[name="scheduleId"]',
					function() {

						var element = $(this).closest('.item');
						var value = ifNotValid($(this).val(), '');

						element.find('.schedule-container').addClass('hidden');
						if (!isEmpty(value)) {
							var data = $(this).select2('data')[0].data;
							//             element.find('.schedule-container>.etd>.date').html(data.sEtd);
							element.find('.schedule-container>.eta>.date')
									.html(data.sEta);
							element.find('.schedule-container').removeClass(
									'hidden');
							element.find('input[name="voyageNo"]').val(
									data.voyageNo);
							element.find('input[name="scheduleId"]').val(
									data.scheduleId);
						}

					})
			//     .on('change', 'select[name="forwarder"]', function() {
			//         var element = $(this).closest('.item');
			//         var frwdrChargeContainer = $(element).find('table.frwdr-charge-table>tbody>tr')
			//         $(frwdrChargeContainer).find('td.frwdr-charge').html('0');
			//         var value = ifNotValid($(this).val(), '');
			//         if (!isEmpty(value)) {
			//             var data = $(this).select2('data')[0].data;
			//             $(frwdrChargeContainer).find('td.freight').html(data.freightCharge);
			//             $(frwdrChargeContainer).find('td.shipping').html(data.shippingCharge);
			//             $(frwdrChargeContainer).find('td.inspection').html(data.inspectionCharge);
			//             $(frwdrChargeContainer).find('td.radiation').html(data.radiationCharge);
			//         }

			//     })
			.on(
					'click',
					"#btn-create-shipping-request",
					function() {

						if (!$('#shipping-arrangement-stock-form').find(
								'.valid-required-fields').valid()) {
							return;
						}
						var containerElement = $('#modal-arrange-shipping')
								.find('.modal-body')
						var shippingRequestItems = {};
						let enteredData = getFormData($(containerElement).find(
								'.data-to-save'));
						shippingRequestItems["forwarderId"] = ifNotValid(
								enteredData.forwarder, '');
						shippingRequestItems["orginCountry"] = ifNotValid(
								enteredData.orginCountry, '');
						shippingRequestItems["orginPort"] = ifNotValid(
								enteredData.orginPort, '');
						shippingRequestItems["destCountry"] = ifNotValid(
								enteredData.destCountry, '');
						shippingRequestItems["destPort"] = ifNotValid(
								enteredData.destPort, '');
						shippingRequestItems["yard"] = ifNotValid(
								enteredData.yard, '');
						var stockDetailsContainer = $(containerElement).find(
								'table#stock-table-details>tbody>tr').not(
								':first')
						var stockShippingInstructionArray = [];
						stockDetailsContainer
								.each(function() {
									var stockDetaisObject = {};
									stockDetaisObject["stockNo"] = ifNotValid(
											$(this).find('td.stockNo').html(),
											0);
									stockDetaisObject["shippingInstructionId"] = ifNotValid(
											$(this).find('td.s-no>input').val(),
											0);
									stockShippingInstructionArray
											.push(stockDetaisObject);
								})
						shippingRequestItems['stockShippingInstructionArray'] = stockShippingInstructionArray;
						shippingRequestItems["scheduleId"] = ifNotValid(
								enteredData.scheduleId, '');
						shippingRequestItems["shippingType"] = ifNotValid(
								enteredData.shippingType, '');
						console.log(JSON.stringify(shippingRequestItems))
						if (!isEmpty(shippingRequestItems)) {
							var result = saveShippingRequest(shippingRequestItems);
							if (result.status = 'success') {
								tableRequestFromSales.ajax.reload();
								$('#modal-arrange-shipping').modal('toggle');

							}
						}
					})
			.on('click', '.btn-remove-item', function() {
				if ($('#modal-arrange-shipping').find('.item').length > 1) {
					$(this).closest('.item').remove();
				}

			})
			.on(
					'change',
					'select[name="customerId"]',
					function() {
						var element = $(this).closest('.item');
						var data = $(this).select2('data');
						$(element)
								.find(
										'select[name="consigneeId"],select[name="notifypartyId"]')
								.empty();
						if (!isEmpty(data[0].data)) {
							var consigneeNotifyParty = data[0].data.consigneeNotifyparties;
							$(element)
									.find('select[name="consigneeId"]')
									.select2(
											{
												allowClear : true,
												width : '100%',
												data : $
														.map(
																consigneeNotifyParty,
																function(item) {
																	return {
																		id : item.id,
																		text : item.cFirstName
																				+ ' '
																				+ item.cLastName
																	};
																})
											}).val('').trigger('change');
							$(element)
									.find('select[name="notifypartyId"]')
									.select2(
											{
												allowClear : true,
												width : '100%',
												data : $
														.map(
																consigneeNotifyParty,
																function(item) {
																	return {
																		id : item.id,
																		text : item.npFirstName
																				+ ' '
																				+ item.npLastName
																	};
																})
											}).val('').trigger('change');
						}

					});
	var shippingCompanyNo;
	$('#shipping-company-filter').on('change', function() {
		shippingCompanyNo = ifNotValid($(this).val(), '');
		table_shipping_requested.draw();
	});
	var shipId;
	$('#ship-filter').on('change', function() {
		shipId = ifNotValid($(this).val(), '');
		table_shipping_requested.draw();
	});

	/*Country port filter in table*/
	$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
		//Request from sales filters
		if (oSettings.sTableId == 'table-request-from-sales') {

			//id filter for notification
			if (!isEmpty(shippingInstructionId)) {
				if (aData[0].length == 0 || aData[0] != shippingInstructionId) {
					return false;
				}
			}
			let forwarder = ifNotValid($(
					'select[name="requested-forwarder-filter"]').val(), '');
			if (typeof forwarder != 'undefined' && forwarder.length != '') {
				if (aData[17].length == 0 || aData[17] != forwarder) {
					return false;
				}

			}
			//type filter 
			if (typeof shipmentTypeFilter != 'undefined'
					&& shipmentTypeFilter.length != '') {
				if (aData[16].length == 0 || aData[16] != shipmentTypeFilter) {
					return false;
				}
			}

			//<!-- ./. added by krishna -->
			// departure date && arrival date
			if (typeof departure_date_min != 'undefined'
					&& departure_date_min.length != '') {
				if (aData[11].length == 0) {
					return false;
				}
				if (typeof aData._date == 'undefined') {
					departure = moment(aData[11], 'MM/YYYY')._d.getTime();
				}
				if (departure_date_min && !isNaN(departure_date_min)) {
					if (departure < departure_date_min) {
						return false;
					}
				}
				if (departure_date_max && !isNaN(departure_date_max)) {
					if (departure > departure_date_max) {
						return false;
					}
				}

			}
			if (typeof arrival_date_min != 'undefined'
					&& arrival_date_min.length != '') {
				if (aData[12].length == 0) {
					return false;
				}
				if (typeof aData._date == 'undefined') {
					arrival = moment(aData[12], 'MM/YYYY')._d.getTime();
				}
				if (arrival_date_min && !isNaN(arrival_date_min)) {
					if (arrival < arrival_date_min) {
						return false;
					}
				}
				if (arrival_date_max && !isNaN(arrival_date_max)) {
					if (arrival > arrival_date_max) {
						return false;
					}
				}

			}
			//country filter 
			if (typeof country != 'undefined' && country.length != '') {
				if (aData[9].length == 0 || aData[9] != country) {
					return false;
				}
			}

			//port filter 
			if (typeof port != 'undefined' && port != null) {
				if (aData[10].length == 0 || aData[10] != port) {
					return false;
				}
			}

		}

		//Available for shipping filters
		if (oSettings.sTableId == 'table-available-for-shipping') {
			//destination country filter 

			let lastLapVehiclesCheck = ifNotValid($('#lastLapVehiclesCheck')
					.is(':checked'), '');
			let destinationCountry = ifNotValid($(
					'#country-filter-available-for-shipping').val(), '');
			let destinationPort = ifNotValid($('#port-filter-for-shipping')
					.val(), '');

			if (typeof destinationCountry != 'undefined'
					&& destinationCountry.length != '') {
				if (aData[6].length == 0 || aData[6] != destinationCountry) {
					return false;
				}

			}
			if (typeof destinationPort != 'undefined'
					&& destinationPort.length != '') {

				if (aData[7].length == 0 || aData[7] != destinationPort) {
					return false;
				}
			}
			//             if ('NZE141-9107830' == 0) {}
			if (typeof lastLapVehiclesCheck != 'undefined'
					&& lastLapVehiclesCheck == true) {
				if (ifNotValid(aData[13], '0') == '0') {
					return false;
				}
			}
		}

		if (oSettings.sTableId == 'table-shipping-requested') {
			//Shipping Company Number filter 
			if (shippingCompanyNo == "") {
				return true
			} else if (typeof shippingCompanyNo != 'undefined'
					&& shippingCompanyNo.length != '') {
				if (aData[1].length == 0 || aData[1] != shippingCompanyNo) {
					return false;
				}
			}
			//Ship Id Filter
			if (shipId == "") {
				return true
			} else if (typeof shipId != 'undefined' && shipId.length != null) {
				if (aData[2].length == 0 || aData[2] != shipId) {
					return false;
				}
			}
		}

		return true;
	});

})
function findAllVessalsAndFwdrByOrginAndDestination(data) {
	var result;
	$.ajax({
		beforeSend : function() {
			$('#spinner').show()
		},
		complete : function() {
			$('#spinner').hide();
		},
		type : "get",
		data : data,
		async : false,
		url : myContextPath + '/shipping/vessalsAndFwdr.json',
		contentType : "application/json",
		success : function(data) {
			result = data;
		}
	});
	return result;
}
function saveShippingRequest(data) {
	var result;
	$.ajax({
		beforeSend : function() {
			$('#spinner').show()
		},
		complete : function() {
			$('#spinner').hide();
		},
		type : "post",
		data : JSON.stringify(data),
		async : false,
		url : myContextPath + '/shipping/request/roro/save',
		contentType : "application/json",
		success : function(data) {
			result = data;
		}
	});
	return result;
}
function saveShippingRequestAcceptDetails(data, id, scheduleId) {
	var result;
	$.ajax({
		beforeSend : function() {
			$('#spinner').show()
		},
		complete : function() {
			$('#spinner').hide();
		},
		type : "post",
		data : JSON.stringify(data),
		async : false,
		url : myContextPath + '/shipping/request/accept?id=' + id
				+ '&scheduleId=' + scheduleId,
		contentType : "application/json",
		success : function(data) {
			result = data;
		}
	});
	return result;
}
function cancelShippingRequest(data) {
	var result;
	$.ajax({
		beforeSend : function() {
			$('#spinner').show()
		},
		complete : function() {
			$('#spinner').hide();
		},
		type : "post",
		async : false,
		data : data,
		url : myContextPath + '/shipping/request/cancel',
		success : function(data) {
			result = data;
		}
	});
	return result;
}
function format(rowData) {
	var element = $('#shipping-requested-details-view>.detail-view').clone();
	element.find('input[name="shipId"]').val(rowData.shipId)
	var tbody = '';
	if (rowData.status != 0) {
		$(element).find('table th.action,td.action').addClass('hidden');
	} else {
		$(element).find('table th.action,td.action').removeClass('hidden');
	}
	var rowClone = $(element).find('table>tbody').find('tr.clone-row');
	for (var i = 0; i < rowData.items.length; i++) {
		var row = $(rowClone).clone();
		row.attr('data-json', JSON.stringify(rowData.items[i]));
		let status = "", className = "";
		if (ifNotValid(rowData.items[i].status, '') == 0) {
			$(row).find('td.status>span.label').addClass('label-default');
			$(row).find('td.status>span.label').html('Initiated');
		} else if (ifNotValid(rowData.items[i].status, '') == 1) {
			$(row).find('td.status>span.label').addClass('label-success');
			$(row).find('td.status>span.label').html('Confirmed');
		} else if (ifNotValid(rowData.items[i].status, '') == 3) {
			$(row).find('td.status>span.label').addClass('label-danger');
			$(row).find('td.status>span.label').html('Cancelled');
		}
		$(row).find('td.s-no').html(i + 1);
		data = rowData.items[i].stockNo == null ? '' : rowData.items[i].stockNo;
		$(row)
				.find('td.stockNo')
				.html(
						'<input type="hidden" name="stockNo" value="'
								+ data
								+ '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="'
								+ data + '">' + data + '</a>');
		$(row).find('td.chassisNo').html(
				ifNotValid(rowData.items[i].chassisNo, ''));
		$(row)
				.find('td.shipmentType')
				.html(
						ifNotValid(rowData.items[i].shippingType, '') == 1 ? 'RORO'
								: ifNotValid(rowData.items[i].shippingType, '') == 2 ? 'CONTAINER'
										: '');
		$(row).find('td.forwarder').html(
				ifNotValid(rowData.items[i].forwarder, ''));
		$(row).find('td.etd').html(ifNotValid(rowData.items[i].sEtd, ''));
		$(row).find('td.eta').html(ifNotValid(rowData.items[i].sEta, ''));

		$(row).removeClass('hide');
		$(element).find('table>tbody').append(row);

	}

	return element;
}

//update remarks to request from sales
function updateRemarks(id, data) {
	var response = "";
	$.ajax({
		beforeSend : function() {
			$('#spinner').show()
		},
		complete : function() {
			$('#spinner').hide();
		},
		type : "post",
		async : false,
		data : JSON.stringify(data),
		url : myContextPath + "/shipping/requestFromSales/remarks?id=" + id,
		contentType : "application/json",
		success : function(data) {
			response = data;
		}
	});
	return response;
}
function initCustomerSelect2(element) {
	//init customer search dropdown
	$(element).select2(
			{
				allowClear : true,
				minimumInputLength : 2,
				width : '100%',
				ajax : {
					url : myContextPath + "/customer/search?flag=customer",
					dataType : 'json',
					delay : 500,
					data : function(params) {
						var query = {
							search : params.term,
							type : 'public'
						}
						return query;

					},
					processResults : function(data) {
						var results = [];
						data = data.data;
						$(this)
						if (data != null && data.length > 0) {
							$.each(data, function(index, item) {
								results.push({
									id : item.code,
									text : item.companyName + ' :: ' + item.firstName
											+ ' ' + item.lastName + '('
											+ item.nickName + ')',
									data : item
								});
							});
						}
						return {
							results : results
						}

					}

				}
			})
}
