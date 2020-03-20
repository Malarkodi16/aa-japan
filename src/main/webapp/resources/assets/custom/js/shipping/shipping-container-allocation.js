var modal_allocate_container_element = $('#modal-container-allocation');
var form_container_allocation_element = modal_allocate_container_element.find('#form-container-allocation-step-1');
var table_shipping_container_ele = $('#table-shipping-container');
var table_shipping_container;
// init step
var container_allocation_step = 0;
$(function() {

    var table_container_allocation_available_element = modal_allocate_container_element.find('#table_container_allocation_available');
    var modal_replace_stock_element = $('#modal-container-shipping-replace-stock');
    var modal_confirm_container = $('#modal-container-shipping-confirm');
    var modal_update_vessel_confirmed = $('#modal-update-vessel-confirmed');
    var table_container_allocated_element = modal_allocate_container_element.find('#table_container_allocation_allocated');
    var container_dropdown_element = modal_allocate_container_element.find('select[name="containers"]');
    var min_container_size = 4;
    var max_container_size = 6;
    var table_container_allocation_available;
    var table_container_allocated;
    initStockSearchDropdown($('#modal-container-shipping-replace-stock #stockNo'))
    modal_allocate_container_element.find('.modal-body').slimScroll({
        start: 'bottom',
        height: ''
    });
    modal_allocate_container_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);

        var flag = triggerElement.attr('data-type');
        if (flag == 'create') {
            let forwarder = $('select[name="requested-forwarder-filter"]').val();
            if (isEmpty(forwarder)) {
                alert($.i18n.prop('shipping.request.validate.forwarder'));
                return event.preventDefault();
            }
            modal_allocate_container_element.find('button#btn-save').attr('data-action', 'create');
            if (tableRequestFromSales.rows({
                selected: true,
                page: 'current'
            }).count() == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return event.preventDefault();

            }
            //             if (isEmpty($('#table-filter-request-from-sales-type').val())) {
            //                 alert($.i18n.prop('common.alert.shipment.type.noselection'));
            //                 return event.preventDefault();
            //             }
            initContainerAllocationModalForCreate(forwarder);
        } else {
            modal_allocate_container_element.find('button#btn-save').attr('data-action', 'edit');
            let tr = triggerElement.closest('tr');
            let data = table_shipping_container.row(tr).data();
            initContainerAllocationModalForEdit(data);
        }
        //         modal_allocate_container_element.find('.select2-select').select2({
        //             allowClear: true,
        //             width: '100%'
        //         });
    }).on('hidden.bs.modal', function() {
        $(this).find('.container-allocation-step').css('display', 'none');
        $(this).find('.select2-select').val('').trigger('change');

    }).on('change', 'select[name="destCountrySelect"]', function() {
        var element = $(this).closest('.item');
        var country = ifNotValid($(this).val(), '');

        var vessalElement = $(element).find('select[name="vessel"]');
        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        var portElement = $(element).find('select[name="destPortSelect"]');
        var yardElement = $(element).find('select[name="yardSelect"]');
        portElement.empty();
        yardElement.empty();
        if (country.length == 0) {
            return;
        }
        var data = filterOneFromListByKeyAndValue(countriesJson, "country", country);
        if (data != null) {
            portElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger('change');

        }

    }).on('change', 'select[name="destPortSelect"]', function() {
        var destPort = ifNotValid($(this).val(), '');
        var element = $(this).closest('.item');
        $(element).find('input[name="destPort"]').val(destPort);
        var vessalElement = $(element).find('select[name="scheduleId"]');
        //         var forwarderElement = $(element).find('select[name="forwarder"]');

        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        //         forwarderElement.empty();
        var data = {};
        //         data["orginCountry"] = ifNotValid($(element).find('select[name="orginCountry"]').val(), '');
        //         data["orginPort"] = ifNotValid($(element).find('select[name="orginPort"]').val(), '');
        data["destCountry"] = ifNotValid($(element).find('select[name="destCountrySelect"]').val(), '');
        data["destPort"] = destPort;
        if (!isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            var forwardersArr = response.forwarders;
            //init vessal dropdown
            vessalElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.scheduleId,
                        text: item.shipName + ' [' + item.shippingCompanyName + ']',
                        data: item
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

    }).on('change', 'select[name="scheduleId"]', function() {

        var element = $(this).closest('.item');
        var value = ifNotValid($(this).val(), '');

        element.find('.schedule-container').addClass('hidden');
        if (!isEmpty(value)) {
            var data = $(this).select2('data')[0].data;
            element.find('.schedule-container>.etd>.date').html(data.sEtd);
            element.find('.schedule-container>.eta>.date').html(data.sEta);
            element.find('.schedule-container').removeClass('hidden');

        }

    }).on('click', "#btn-save", function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if ($(this).attr('data-action') == 'create') {
            createContainerShippingRequest();
        } else {
            editContainerShippingRequest();
        }
    })
    //create new container shipping request
    function createContainerShippingRequest() {
        var shippingRequestItems = [];
        let scheduleDetails = {};
        scheduleDetails["orginCountry"] = ifNotValid($(modal_allocate_container_element).find('select[name="orginCountry"]').val(), '');
        scheduleDetails["orginPort"] = ifNotValid($(modal_allocate_container_element).find('select[name="orginPort"]').val(), '');
        scheduleDetails["destCountry"] = ifNotValid($(modal_allocate_container_element).find('select[name="destCountrySelect"]').val(), '');
        scheduleDetails["destPort"] = ifNotValid($(modal_allocate_container_element).find('select[name="destPortSelect"]').val(), '');
        scheduleDetails["scheduleId"] = ifNotValid($(modal_allocate_container_element).find('select[name="scheduleId"]').val(), '');
        scheduleDetails["forwarderId"] = ifNotValid($(modal_allocate_container_element).find('select[name="forwarder"]').val(), '');
        //container
        //container allocation details
        var table_container_allocated_details = table_container_allocated.rows().data().toArray();
        if (table_container_allocated_details.length == 0) {
            alert($.i18n.prop('alert.shipping.request.no.stock.allocatted'));
            return false;
        }
        for (var i = 0; i < table_container_allocated_details.length; i++) {
            for (var j = 0; j < table_container_allocated_details[i].container_items.length; j++) {
                var data = {}
                data["shippingInstructionId"] = ifNotValid(table_container_allocated_details[i].container_items[j].id, '');
                data["stockNo"] = ifNotValid(table_container_allocated_details[i].container_items[j].stockNo, '');
                data["customerId"] = ifNotValid(table_container_allocated_details[i].container_items[j].customerId, '');
                data["consigneeId"] = ifNotValid(table_container_allocated_details[i].container_items[j].consigneeId, '');
                data["notifypartyId"] = ifNotValid(table_container_allocated_details[i].container_items[j].notifypartyId, '');
                data["orginCountry"] = scheduleDetails["orginCountry"];
                data["orginPort"] = scheduleDetails["orginPort"];
                data["destCountry"] = scheduleDetails["destCountry"];
                data["destPort"] = scheduleDetails["destPort"];
                data["scheduleId"] = scheduleDetails["scheduleId"];
                data["forwarderId"] = scheduleDetails["forwarderId"];
                data["shippingType"] = 2;
                data["containerNo"] = table_container_allocated_details[i].container_id;
                shippingRequestItems.push(data);
            }
        }
        createContainerShippingRequest
        if (shippingRequestItems.length > 0) {
            var result = saveContainerAllocation(shippingRequestItems);
            if (result.status = 'success') {
                tableRequestFromSales.ajax.reload();
                modal_allocate_container_element.modal('toggle');

            }
        }
    }
    //edit and update container shipping request
    function editContainerShippingRequest() {
        var shippingRequestItems = [];
        let scheduleDetails = {};
        scheduleDetails["orginCountry"] = ifNotValid($(modal_allocate_container_element).find('select[name="orginCountry"]').val(), '');
        scheduleDetails["orginPort"] = ifNotValid($(modal_allocate_container_element).find('select[name="orginPort"]').val(), '');
        scheduleDetails["destCountry"] = ifNotValid($(modal_allocate_container_element).find('select[name="destCountrySelect"]').val(), '');
        scheduleDetails["destPort"] = ifNotValid($(modal_allocate_container_element).find('select[name="destPortSelect"]').val(), '');
        scheduleDetails["scheduleId"] = ifNotValid($(modal_allocate_container_element).find('select[name="scheduleId"]').val(), '');
        scheduleDetails["forwarderId"] = ifNotValid($(modal_allocate_container_element).find('select[name="forwarder"]').val(), '');
        //container
        //container allocation details
        var table_container_allocated_details = table_container_allocated.rows().data().toArray();
        var table_container_allocation_available_data = table_container_allocation_available.rows().data().toArray()
        if (table_container_allocated_details.length == 0) {
            alert($.i18n.prop('alert.shipping.request.no.stock.allocatted'));
            return false;
        }
        for (var i = 0; i < table_container_allocated_details.length; i++) {
            for (var j = 0; j < table_container_allocated_details[i].container_items.length; j++) {
                var data = {}
                data["shipmentRequestId"] = table_container_allocated_details[i].container_items[j]["id"];
                data["stockNo"] = ifNotValid(table_container_allocated_details[i].container_items[j].stockNo, '');
                data["orginCountry"] = scheduleDetails["orginCountry"];
                data["orginPort"] = scheduleDetails["orginPort"];
                data["destCountry"] = scheduleDetails["destCountry"];
                data["destPort"] = scheduleDetails["destPort"];
                data["scheduleId"] = scheduleDetails["scheduleId"];
                data["forwarderId"] = scheduleDetails["forwarderId"];
                data["containerNo"] = table_container_allocated_details[i].container_id;
                data["status"] = 0;
                shippingRequestItems.push(data);
            }
        }
        for (var i = 0; i < table_container_allocation_available_data.length; i++) {
            var data = {}
            data["stockNo"] = table_container_allocation_available_data[i]["stockNo"];
            data["shipmentRequestId"] = table_container_allocation_available_data[i]["id"];
            data["status"] = -1;
            shippingRequestItems.push(data);
        }
        if (shippingRequestItems.length > 0) {
            var result = editContainerAllocation(shippingRequestItems);
            if (result.status = 'success') {
                table_shipping_container.ajax.reload();
                modal_allocate_container_element.modal('toggle');

            }
        }
    }
    table_container_allocated_element.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_container_allocated.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_container_allocated.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_container_allocated.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            row.child(showAllocattedItems(row.data(), row.index())).show();

            tr.addClass('shown');
        }
    });
    table_container_allocated_element.on('click', 'td a.btn-remove', function() {
        var data = $(this).closest('li').attr('data-json');
        data = JSON.parse(data);
        var row_index = $(this).closest('li').attr('data-index');
        var row_data = table_container_allocated.row(row_index).data();
        row_data["container_items"] = row_data["container_items"].filter(function(item, idx) {
            return item.id != data.id;
        });
        if (row_data["container_items"].length == 0) {
            table_container_allocated.row(row_index).remove().draw()
        } else {
            table_container_allocated.row(row_index).data(row_data).invalidate();
            $(this).closest('li').remove();
        }

        //re allocate to available stock
        table_container_allocation_available.rows.add([data]).draw();

    })
    function initContainerAllocationModalForCreate(forwarder) {

        container_allocation_step = 0;
        //init multi step form
        showTab(container_allocation_step)
        var selectedRowData = tableRequestFromSales.rows({
            selected: true,
            page: 'current'
        }).data().toArray();
        var data = selectedRowData.map(request=>({
            "id": request.id,
            "stockNo": request.stockNo,
            "chassisNo": request.chassisNo,
            "customerId": request.customerId,
            "consigneeId": request.consigneeId,
            "notifypartyId": request.notifyPartyId,
            "originCountry": request.originCountry,
            "originPort": request.originPort,
            "destCountry": request.destinationCountry,
            "destPort": request.destinationPort,
            "maker": request.maker,
            "model": request.model
        }));

        // value populate for create
        modal_allocate_container_element.find('select[name="orginCountry"]').val(ifNotValid(data[0].originCountry, '')).trigger('change');
        modal_allocate_container_element.find('select[name="orginPort"]').val(ifNotValid(data[0].originPort, '')).trigger('change');
        modal_allocate_container_element.find('select[name="destCountrySelect"]').val(ifNotValid(data[0].destCountry, '')).trigger('change');
        modal_allocate_container_element.find('select[name="destPortSelect"]').val(ifNotValid(data[0].destPort, '')).trigger('change');
        modal_allocate_container_element.find('select[name="forwarder"]').val(forwarder).trigger('change');
        //         modal_allocate_container_element.find('select[name="destPortSelect"]').trigger('change');

        var total_containers = Math.ceil(data.length / min_container_size);
        let options_list = [];
        for (var i = 1; i <= total_containers; i++) {
            options_list.push(i);

        }

        initSelect2ForContainerDropdown(container_dropdown_element, options_list);
        initAvailableForAllocateDatatable(data);
        initContainerAllocatedDatatable([])
    }
    function initContainerAllocationModalForEdit(rowdata) {
        modal_allocate_container_element.find('select[name="orginCountry"]').val(ifNotValid(rowdata.orginCountry, '')).trigger('change');
        modal_allocate_container_element.find('select[name="orginPort"]').val(ifNotValid(rowdata.orginPort, '')).trigger('change');
        modal_allocate_container_element.find('select[name="destCountrySelect"]').val(ifNotValid(rowdata.destCountry, '')).trigger('change');
        modal_allocate_container_element.find('select[name="destPortSelect"]').val(ifNotValid(rowdata.destPort, '')).trigger('change');
        modal_allocate_container_element.find('select[name="scheduleId"]').val(ifNotValid(rowdata.scheduleId, '')).trigger('change');
        modal_allocate_container_element.find('select[name="forwarder"]').val(ifNotValid(rowdata.forwarder, '')).trigger('change');
        container_allocation_step = 0;
        //init multi step form
        showTab(container_allocation_step)
        var allocatted_items = rowdata.items;
        var allocatted_data = [];
        let options_list = [];
        for (let i = 0; i < allocatted_items.length; i++) {
            var data = {};
            options_list.push(allocatted_items[i].containerNo);
            data["container_id"] = allocatted_items[i].containerNo;
            var container_items = allocatted_items[i].items;
            data["container_items"] = container_items.map(request=>({
                "id": request.shipmentRequestId,
                "stockNo": request.stockNo,
                "chassisNo": request.chassisNo,
                "customerId": request.customerId,
                "consigneeId": request.consigneeId,
                "notifypartyId": request.notifypartyId,
                "maker": request.maker,
                "model": request.model
            }));
            allocatted_data.push(data);
        }

        initSelect2ForContainerDropdown(container_dropdown_element, unique(options_list));
        initAvailableForAllocateDatatable([]);
        initContainerAllocatedDatatable(allocatted_data)
    }
    //init datatable for available stock for container allocation
    function initAvailableForAllocateDatatable(data) {
        table_container_allocation_available = table_container_allocation_available_element.DataTable({
            "dom": '<<t>>',
            "pageLength": 25,
            "destroy": true,
            "language": {
                "zeroRecords": " "
            },
            "bPaginate": false,
            "data": data,
            select: {
                style: 'multi',
                selector: 'td:first-child>input'
            },
            columnDefs: [{
                "targets": '_all',
                "defaultContent": ""
            }, {
                "targets": 0,

                "className": 'select-checkbox',
                "data": "id",
                "render": function(data, type, row) {
                    data = data == null ? '' : data;
                    if (type === 'display') {
                        return '<input class="selectBox" type="checkbox" value="' + data + '">'
                    }
                    return data;
                }
            }, {
                targets: 1,

                "data": "chassisNo"
            }, {
                targets: 2,
                "className": "details-control",
                "data": "maker",
                "render": function(data, type, row) {
                    data = data == null ? '' : data;
                    if (type === 'display') {
                        data = row.maker + " / " + row.model
                    }
                    return data;
                }
            }, {
                targets: 3,
                "className": "details-control",
                "data": "length"
            }, {
                targets: 4,

                "className": "details-control",
                "data": "width"
            }, {
                targets: 5,

                "className": "details-control",
                "data": "height"
            }, {
                targets: 6,
                "data": "m3",

            }]
        });
    }
    //init datatable for allocation table
    function initContainerAllocatedDatatable(data) {
        table_container_allocated = table_container_allocated_element.DataTable({
            "dom": '<<t>>',
            "pageLength": 25,
            "destroy": true,
            "language": {
                "zeroRecords": " "
            },
            "bPaginate": false,
            "data": data,
            columnDefs: [{
                "targets": '_all',
                "defaultContent": ""
            }, {
                "targets": 0,
                "className": 'details-control',
                "data": "container_id",
                "render": function(data, type, row) {
                    data = data == null ? '' : data;
                    if (type === 'display') {
                        var html = '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">Container - ' + row.container_id + '</span><div class="action-container pull-right"><span class="label label-info pull-right">' + (max_container_size - row.container_items.length) + ' available of ' + max_container_size + '</span></div></h5></div></div></div></div>'
                        return html;
                    }
                    return data;
                }
            }],
            "createdRow": function(row, data, dataIndex) {
                $(row).attr('data-container', data.container_id);
            },
            "fnDrawCallback": function(oSettings) {
                $(oSettings.nTHead).hide();
            }
        });
    }
    $(modal_allocate_container_element).on('click', '#btn-allocate-container', function() {

        var container_id = container_dropdown_element.val();
        if (isEmpty(container_id)) {
            return false;
        }
        var rows = table_container_allocation_available.rows({
            selected: true,
            page: 'current'
        });
        if (rows.count() == 0) {
            return false;
        }
        var selectedRowData = rows.data().toArray();
        var data = {};
        data["container_id"] = container_id;
        data["container_items"] = selectedRowData;
        if (allocate(container_id, data)) {
            rows.remove();
            table_container_allocation_available.draw();
        }

    })

    function allocate(container, data) {
        if (!isSpaceAvailable(container, data.container_items.length)) {
            alert($.i18n.prop('alert.shipping.request.container.nospace'));
            return false;
        }
        var row = table_container_allocated.row(table_container_allocated.$('tr[data-container="' + container + '"]')).toArray();
        if (row.length == 0) {
            table_container_allocated.rows.add([data]).draw();
        } else {
            var row_data = table_container_allocated.row(row[0]).data();
            row_data["container_items"] = row_data["container_items"].concat(data["container_items"]);
            table_container_allocated.row(row[0]).data(row_data).invalidate();
            table_container_allocated.draw();
        }
        return true;
    }
    function isContainerExist() {
        var data = table_container_allocated.row(table_container_allocated.$('tr[data-container="' + container + '"]')).data();
        return isEmpty(data) ? false : true;
    }
    function isSpaceAvailable(container, noofstock) {
        var available_space = 0;
        var data = table_container_allocated.row(table_container_allocated.$('tr[data-container="' + container + '"]')).data();
        if (!isEmpty(data)) {
            available_space = data.container_items.length
        }
        return (available_space + noofstock) <= max_container_size ? true : false;
    }
    var exportoptions = {
        filename: function() {
            var d = new Date();
            return 'Container_Request_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
        },
        attr: {
            type: "button",
            id: 'dt_excel_export'
        },
        title: ''
    };
    table_shipping_container = table_shipping_container_ele.DataTable({
        "dom": '<ip<t>ip>',
        "pageLength": 25,
        "ajax": {
            "url": myContextPath + "/shipping/shipment-container",
            "data": function(data) {
                data["draw"] = $('#shipping-container').hasClass('hidden') ? false : true
                data["show"] = $('#container-shipment-filter input[name="containerAllcationStatus"]:checked').val();
                return data;
            }
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    let actionHtml = '';
                    let current_status_text = '';
                    let next_status_text = '';
                    let statusHtml = '';
                    let next_status;
                    if (row.shippingStatus == 1) {
                        current_status_text = 'Initiated';
                        next_status_text = 'In-Transit';
                        next_status = 2;
                    } else if (row.shippingStatus == 2) {
                        current_status_text = 'In-Transit';
                        next_status_text = 'Completed';
                        next_status = 3;
                    } else if (row.shippingStatus == 3) {
                        current_status_text = 'Completed';
                    }

                    if (row.status == 0) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-container-allocation" data-type="edit"><i class="fa fa-fw fa-pencil-square-o"></i>Edit Allocation</a>';
                        actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs export-excel"><i class="fa fa-fw  fa-file-excel-o"></i>Export Excel</a>';
                    } else if (row.status == 4) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs btn-confirm-vessel" data-type="allocation" data-allocationid="' + row.allocationId + '" data-containerno="' + ifNotValid(row.containerNo, '') + '"><i class="fa fa-fw fa-check"></i>Confirm Vessel</a>';
                    } else if (row.status == 5) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-flag="container" data-target="#modal-update-vessel-confirmed"><i class="fa fa-fw fa-pencil"></i>Update BL</a>';
                        statusHtml = '<span class="label label-info ml-5" style="min-width:100px">' + current_status_text + '</span>'
                        if (row.shippingStatus != 3) {
                            actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs btn-update-status" data-type="allocation" data-allocationid="' + row.allocationId + '" data-next-status="' + next_status + '"><i class="fa fa-fw fa-check"></i>Update [' + next_status_text + ']</a>';
                        }
                    }

                    return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.shippingCompanyName + ' - ' + row.vessel + ' [' + row.voyageNo + '] - ' + row.allocationId + '</span>' + statusHtml + '</h5></div><div class="action-container pull-right">' + actionHtml + '</div></div></div></div>';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "shippingCompanyNo",
            "visible": false
        }, {
            targets: 2,
            "className": "details-control",
            "data": "shipId",
            "visible": false
        }, {
            targets: 3,
            "className": "details-control",
            "data": "voyageNo",
            "visible": false
        }],
        buttons: [$.extend(true, {}, exportoptions, {
            extend: 'excelHtml5'
        })],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }

    });
    table_shipping_container.on('click', 'a.export-excel', function() {
        let temp_excel_data_ele = $('<input>').attr({
            type: 'hidden',
            id: 'temp_excel_data'
        })
        let temp_excel_data = table_shipping_container.row($(this).closest('tr')).data();
        temp_excel_data_ele.val(JSON.stringify(temp_excel_data));

        temp_excel_data_ele.appendTo('body');
        table_shipping_container.button("#dt_excel_export").trigger();
    })
    jQuery.fn.DataTable.Api.register('buttons.exportData()', function(options) {
        if (this.context.length) {
            let temp_excel_data = $('#temp_excel_data').val();
            temp_excel_data = JSON.parse(temp_excel_data);
            $('#temp_excel_data').remove();
            var reportData = [];
            for (var i = 0; i < temp_excel_data.items.length; i++) {
                for (var j = 0; j < temp_excel_data.items[i].items.length; j++) {
                    var data = {};
                    data["container_no"] = temp_excel_data.items[i].items[j].containerNo;
                    data["chassisNo"] = temp_excel_data.items[i].items[j].chassisNo;
                    data["maker"] = temp_excel_data.items[i].items[j].maker;
                    data["model"] = temp_excel_data.items[i].items[j].model;
                    data["ctm"] = '';
                    data["delivery"] = '';
                    data["cr"] = '';
                    reportData.push(data);
                }

            }
            var columnDefs = [{
                'data': 'container_no',
                'name': 'Container No'
            }, {
                'data': 'chassisNo',
                'name': 'Chassis No'
            }, {
                'data': 'maker',
                'name': 'Maker'
            }, {
                'data': 'model',
                'name': 'Model'
            }, {
                'data': 'ctm',
                'name': 'CTM'
            }, {
                'data': 'delivery',
                'name': 'Delivery'
            }, {
                'data': 'cr',
                'name': 'CR'
            }];
            let header = columnDefs.map((col)=>col.name);
            return {
                body: formatExcelData(reportData, columnDefs),
                header: header
            };
        }
    });
    function formatExcelData(data, header) {
        let results = data.map((item)=>{
            var result = [];
            for (let i = 0; i < header.length; i++) {
                var value = ifNotValid(item[header[i].data], '')
                result.push(value)
            }
            return result
        }
        )
        return results
    }
    table_shipping_container.on('click', 'td div.details-container.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_shipping_container.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_shipping_container.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_shipping_container.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                    $(row.node()).removeClass('shown');
                }

            })
            row.child(containerFormat(row.data())).show();
            tr.addClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        }
    });
    //confirm vessel
    table_shipping_container.on('click', 'a.btn-confirm-vessel', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        let type = $(this).attr('data-type');
        let allocationId = ifNotValid($(this).attr('data-allocationid'), '');
        let containerNo = ifNotValid($(this).attr('data-containerno'), ';');
        let response = confirmVessel({
            "allocationId": allocationId,
            "containerNo": containerNo
        }, type);
        if (response.status == 'success') {
            table_shipping_container.ajax.reload();
        }
    })
    //update shipping status
    table_shipping_container.on('click', 'a.btn-update-status', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        let data_next_status = $(this).attr('data-next-status');
        let allocationId = ifNotValid($(this).attr('data-allocationid'), '');
        let response = updateShippingStatus(allocationId, data_next_status);
        if (response.status == 'success') {
            table_shipping_container.ajax.reload();
        }
    })
    //expand container details
    table_shipping_container.on('click', 'tr li.container-item', function() {

        if ($(this).next('li.container-item-details').length > 0) {
            $(this).next('li.container-item-details').remove();
            $(this).removeClass('shown');

        } else {
            let data = $(this).attr('data-json');
            data = JSON.parse(data);
            $(this).parent().find('li.container-item-details').remove();
            $(this).after($('<li class="list-group-item container-item-details"></li>').append(format1(data)));
            //append('<li class="list-group-item container-item details"></li>').html()
            //             row.child(containerFormat(row.data())).show();
            $(this).addClass('shown');

        }
    });
    //confirm container stock
    modal_confirm_container.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var data = triggerElement.closest('li').attr('data-json');
        data = JSON.parse(data);
        $(this).find('#rowData').val(JSON.stringify(data));
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
        $(this).find('#rowData').attr('data-json', '');
    }).on('click', '#btn-save', function() {

        if (!modal_confirm_container.find('form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowdata = modal_confirm_container.find('#rowData').val();
        rowdata = JSON.parse(rowdata);
        let containerNo = rowdata.containerNo;
        let allocationId = rowdata.allocationId;
        let data = {}
        data["containerName"] = modal_confirm_container.find('input[name="containerName"]').val();
        data["slaNo"] = modal_confirm_container.find('input[name="slaNo"]').val();
        var response = confirmContainerAllocation(data, allocationId, containerNo)
        if (response.status == 'success') {
            table_shipping_container.ajax.reload();
            modal_confirm_container.modal('toggle');
        }
    })
    //update shipment complete details
    modal_update_vessel_confirmed.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var flag = triggerElement.attr('data-flag');
        var data;
        if (flag == "container") {
            data = table_shipping_container.row(triggerElement.closest('tr')).data();
        } else if (flag == "roro") {
            data = table_shipping_requested.row(triggerElement.closest('tr')).data();
        }

        $(this).find('#rowData').val(JSON.stringify(data));
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
        $(this).find('#rowData').attr('data-json', '');
    }).on('click', '#btn-save', function() {

        if (!modal_update_vessel_confirmed.find('form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        let rowdata = {};
        rowdata = modal_update_vessel_confirmed.find('#rowData').val();
        rowdata = JSON.parse(rowdata);
        let allocationId = rowdata.allocationId;
        let scheduleId = rowdata.scheduleId;
        let data = {}
        var response;
        data["blNo"] = modal_update_vessel_confirmed.find('input[name="blNo"]').val();
        if (!isEmpty(allocationId)) {
            response = updateVesselConfirmed(data, allocationId)
        } else if (!isEmpty(scheduleId)) {
            response = updateRoroConfirmed(data, scheduleId)
        }

        if (response.status == 'success') {
            table_shipping_container.ajax.reload();
            modal_update_vessel_confirmed.modal('toggle');
        } else if (response.status == 'successRoro') {
            table_shipping_requested.ajax.reload();
            modal_update_vessel_confirmed.modal('toggle');
        }
    })
    //replace container stock
    modal_replace_stock_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var data = triggerElement.closest('tr').attr('data-json');
        data = JSON.parse(data);
        $(this).find('#rowData').val(JSON.stringify(data));
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');

    }).on('click', '#btn-save', function() {
        if (!modal_replace_stock_element.find('form').valid()) {
            return false;
        }
        let stockNo = modal_replace_stock_element.find('select[name="stockNo"]').val();
        var data = modal_replace_stock_element.find('#rowData').val();
        data = JSON.parse(data);
        var response = replaceContainerStock(data.shipmentRequestId, stockNo)
        if (response.status == 'success') {
            table_shipping_container.ajax.reload();
            modal_replace_stock_element.modal('toggle');
        }
    })
})
function initSelect2ForContainerDropdown(element, list) {

    element.empty().select2({
        allowClear: true,
        width: '100%',
        data: $.map(list, function(item) {
            return {
                id: item,
                text: item
            };
        })
    }).val('').trigger('change');
}
function initStockSearchDropdown(element) {
    element.select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });
}
function showAllocattedItems(data, rowIdx) {
    var element = $('#cloneable-items>#shipping-container-allocation-table-allocatted>.clone-element').clone();
    var clone_element = $(element).find('ul#item-container li.clone-row.hide');
    for (var i = 0; i < data.container_items.length; i++) {
        var row = $(clone_element).clone();
        row.attr('data-json', JSON.stringify(data.container_items[i]));
        row.attr('data-index', rowIdx);
        row.find('span.chassisNo').html(data.container_items[i]["chassisNo"])
        $(row).removeClass('hide');
        $(element).find('ul#item-container').append(row);
    }
    return element;
}
// Display the current tab

function showTab(n) {
    // This function will display the specified tab of the form ...
    var x = modal_allocate_container_element.find('.container-allocation-step');
    $(x[n]).css('display', 'block');
    // ... and fix the Previous/Next buttons
    if (n == 0) {
        modal_allocate_container_element.find('#btn-previous-step').css('display', 'none');

    } else {
        modal_allocate_container_element.find('#btn-previous-step').css('display', 'inline');

    }
    if (n == (x.length - 1)) {
        modal_allocate_container_element.find('#btn-next-step').css('display', 'none');
        modal_allocate_container_element.find('#btn-previous-step,#btn-save').css('display', 'inline');
    } else {
        modal_allocate_container_element.find('#btn-next-step').css('display', 'inline');
        modal_allocate_container_element.find('#btn-save').css('display', 'none');
    }
    // ... and run a function that displays the correct step indicator:
    fixStepIndicator(n)
}

function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = modal_allocate_container_element.find('span.step');
    //     var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        $(x[i]).removeClass('active');
        //.css('background-color', '');
    }
    //... and adds the "active" class to the current step:
    $(x[n]).addClass('active');

}
function containerAllcationStepNextPrev(n) {
    // This function will figure out which tab to display
    var x = modal_allocate_container_element.find('.container-allocation-step');
    //     var x = document.getElementsByClassName("tab");
    // Exit the function if any field in the current tab is invalid:
    if (n == 1 && !form_container_allocation_element.valid()) {
        return false;
    } else if (n == 1 && form_container_allocation_element.valid()) {
        $(modal_allocate_container_element.find('.step')[container_allocation_step]).css('background-color', '#4CAF50');
    }

    // Hide the current tab:
    $(x[container_allocation_step]).css('display', 'none');
    // Increase or decrease the current tab by 1:
    container_allocation_step = container_allocation_step + n;
    // if you have reached the end of the form... :
    if (container_allocation_step >= x.length) {
        return false;
    }
    // Otherwise, display the correct tab:
    showTab(container_allocation_step);
}
function containerFormat(rowData) {
    let ul = $('<ul style="width:90%"></ul>');
    for (var i = 0; i < rowData.items.length; i++) {
        let json = JSON.stringify(rowData.items[i]);
        var actionHtml = '';
        var description = 'CONTAINER-' + rowData.items[i].containerNo;
        if (rowData.items[i].status == 0) {
            actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs pull-right" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-container-shipping-confirm"><i class="fa fa-fw fa-check"></i>Confirm Container</a>';
        } else {
            description += ' [' + rowData.items[i].containerName + ']'
        }
        if (rowData.items[i].status == 4) {

            actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs pull-right btn-confirm-vessel" data-type="container" data-allocationid="' + rowData.items[i].allocationId + '" data-containerno="' + rowData.items[i].containerNo + '"><i class="fa fa-fw fa-check"></i>Confirm Vessel</a>';
        }
        var li = $('<li class="list-group-item container-item"><span class="description"></span>' + actionHtml + '</div></li>');

        li.find('span.description').html(description)
        li.attr('data-json', json);
        $(ul).append(li);
    }
    return ul;
}

function format1(rowData) {
    var element = $('#shipping-container-stock-details-view .item-container').clone();
    //     element.find('input[name="shipId"]').val(rowData.shipId)
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
        if (rowData.items[i].status == 0) {
            $(row).find('td.status>span.label').addClass('label-default');
            $(row).find('td.status>span.label').html('Initiated');
        }
        $(row).find('td.s-no').html(i + 1);
        data = rowData.items[i].stockNo == null ? '' : rowData.items[i].stockNo;
        $(row).find('td.stockNo').html('<input type="hidden" name="stockNo" value="' + rowData.items[i].stockNo + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>');
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.items[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.items[i].model, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
//confirm container
function confirmContainerAllocation(data, allocationId, containerNo) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/confirm?allocationId=' + allocationId + '&containerNo=' + containerNo,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//update update vessel confirmed
function updateVesselConfirmed(data, allocationId) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/update/vessel/confirmed?allocationId=' + allocationId,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function updateRoroConfirmed(data, scheduleId) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/update/roro/confirmed?scheduleId=' + scheduleId,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//save container allocation
function replaceContainerStock(shipmentRequestId, stockNo) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/replace/stock?shipmentRequestId=' + shipmentRequestId + '&stockNo=' + stockNo,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//save container allocation
function saveContainerAllocation(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/save',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//save container allocation
function editContainerAllocation(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/edit',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//confirm vessel
function confirmVessel(data, type) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/container/allocation/confirmVessel?type=' + type,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//update shipping status
function updateShippingStatus(allocationId, action) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",

        async: false,
        url: myContextPath + '/shipping/container/allocation/update/shipping/status?allocationId=' + allocationId + '&action=' + action,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
