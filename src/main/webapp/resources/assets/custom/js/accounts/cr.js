var crTransactionDetails = $('#crTransactionDetails');
var crTransactionDetailsDetailTable;
var tableEle = crTransactionDetails.find('table')
$(function() {

    $('#custId').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
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
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')',
                            data: item
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    });

    $.getJSON(myContextPath + "/user/getUser-list", function(data) {
        var salesJson = data.data;
        var ele = $('#select_staff');
        $(ele).select2({
            allowClear: true,
            width: '100%',
            data: $.map(salesJson, function(item) {
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        }).val('').trigger("change");
    });

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        var elements = $('select[name="destCountry"]');
        let crManageArray = countryJson.filter(function(index) {
            return index.country == "GEORGIA" || index.country == "AUSTRALIA" || index.country == "NEW ZEALAND" || index.country == "RUSSIA";
        })
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(crManageArray, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    $.getJSON(myContextPath + "/data/shipname.json", function(data) {
        shipNameJson = data;
        var vessalAndVoyageNoEle = $('#vessalAndVoyageNo');
        vessalAndVoyageNoEle.empty();
        vessalAndVoyageNoEle.select2({
            allowClear: true,
            width: '100%',
            data: $.map(shipNameJson, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });

    let pageName = $('input[name="pageName"]').val();
    var tableEle = $('#table-cr-data');
    var table = tableEle.DataTable({

        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accountsBL/cr-management/list",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "chassisNo",
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
            "width": "150px",
            "data": "customer"
        }, {
            targets: 3,
            "data": "staff"
        }, {
            targets: 4,
            "data": "destinationPortName"
        }, {
            targets: 5,
            "data": "vessalNo"
        }, {
            targets: 6,
            "data": "vessalName"
        }, {
            targets: 7,
            "data": "lotNo"
        }, {
            targets: 8,
            "data": "blNo"
        }, {
            targets: 9,
            "data": "etd"
        }, {
            targets: 10,
            "data": "eta"
        }, {
            targets: 11,
            "className": "dt-right",
            "data": "soldAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 12,
            "className": "dt-right",
            "data": "amountReceived",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 13,
            "className": "dt-right",
            "data": "balanceAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 14,
            "data": "recSurStatus",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == "0") {
                    status = "Idle"
                    className = "default"
                } else if (data == "1") {
                    status = "RECEIVED"
                    className = "primary"

                } else if (data == "2") {
                    status = "SURRENDER"
                    className = "primary"

                }
                return html = '<span class="label label-' + ifNotValid(className, 'default') + '" style="min-width:100px">' + ifNotValid(status, 'NA') + '</span>';
            }
        }, {
            targets: 15,
            "data": "blDocumentStatus",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == "0") {
                    status = "Idle"
                    className = "default"
                } else if (data == "1") {
                    status = "RECEIVE"
                    className = "info"

                } else if (data == "2") {
                    status = "ISSUED"
                    className = "primary"

                } else if (data == "3") {
                    status = "DISPATCHED"
                    className = "info"

                } else if (data == "4") {
                    status = "SURRENDER"
                    className = "warning"

                }
                return html = '<span class="label label-' + ifNotValid(className, 'default') + '" style="min-width:100px">' + ifNotValid(status, 'NA') + '</span>';
            }
        }, {
            targets: 16,
            "data": "",
            "width": "200px",
            "render": function(data, type, row) {
                var html = '';
                let show = pageName == "sales" ? "hidden" : ""
                if (row.showBlAmendBtn == 1) {
                    if (row.recSurStatus != 2) {
                        html += '<a class="ml-5 btn btn-default btn-xs" id="single-update-cr" value="single-update" title="Change Consignee" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-update-cr-amend"><i class="fa fa-refresh"></i></a>'
                        html += '<a class="ml-5 btn btn-primary btn-xs" id="single-update-bl-no" value="single-update-bl-no" title="Edit Bl No" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-update-bl-no"><i class="fa fa-info-circle"></i></a>'
                    }
                    html += '<button type="button" id="view-transactions" class="btn btn-warning ml-5 btn-xs" data-target="#modal-cr-transaction-list" title="View Trnsactions" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                    if ((row.recSurStatus == 0 || row.recSurStatus == 1) && (row.blDocumentStatus == 0)) {
                        html += '<button type="button" id="rec-sur-status" class="btn btn-info ml-5 btn-xs ' + show + '" data-target="#modal-update-Rec-Sur-status" title="Receive Or Surrender" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-registered"></i></button>'
                    }
                }
                if (row.recSurStatus == 1) {
                    if (row.blDocumentStatus == 0)
                        html += '<a class="ml-5 btn btn-default btn-xs ' + show + '" name="crDocumentStatus_receive"><i class="fa fa-refresh"></i> Receive</a>'
                    if (row.blDocumentStatus == 1)
                        html += '<a class="ml-5 btn btn-default btn-xs ' + show + '" name="crDocumentStatus_issue"><i class="fa fa-refresh"></i> Issue</a>'
                    if (row.blDocumentStatus == 2)
                        html += '<a class="ml-5 btn btn-default btn-xs ' + show + '" name="crDocumentStatus_dispatch"><i class="fa fa-refresh"></i> Dispatched</a>'
                }
                return html;
            }
        }, {
            targets: 17,
            "visible": false,
            "data": "customerId"
        }, {
            targets: 18,
            "visible": false,
            "data": "consigneeId"
        }, {
            targets: 19,
            "visible": false,
            "data": "staffId"
        }, {
            targets: 20,
            "visible": false,
            "data": "destinationCountry"
        }, {
            targets: 21,
            "visible": false,
            "data": "vessalNo"
        }],
        "drawCallback": function(settings, json) {
            $('#table-bl-data').find('span.autonumber').autoNumeric('init')
        }
    });

    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });

    var etdDate;
    $('#table-filter-etd-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        etdDate = $(this).val();
        $(this).closest('.input-group').find('.etd-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon etd-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.etd-clear-date', function() {
        etdDate = '';
        $('#table-filter-etd-date').val('');
        $(this).remove();
        table.draw();

    })

    var etaDate;
    $('#table-filter-eta-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        etaDate = $(this).val();
        $(this).closest('.input-group').find('.eta-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon eta-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.eta-clear-date', function() {
        etaDate = '';
        $('#table-filter-eta-date').val('');
        $(this).remove();
        table.draw();

    });

    var filterCustomer, filterStaff, filterConsignee, filterCountry, filterPort, filterVessal;
    $('#custId').on('change', function() {
        filterCustomer = $(this).find('option:selected').val();
        var consignee = $(this).find(':selected').data('data');
        var data = consignee.data
        table.draw();
        $('#consigneeId').empty();
        if (!isEmpty(data)) {
            let consArray = data.consigneeNotifyparties.filter(function(index) {
                return !isEmpty(index.cFirstName);
            })
            $('#consigneeId').select2({
                allowClear: true,
                width: '100%',
                data: $.map(consArray, function(item) {
                    return {
                        id: item.id,
                        text: item.cFirstName
                    };
                })
            }).val('').trigger('change');
        }
    });

    $('#consigneeId').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        filterConsignee = $(this).find('option:selected').val();
        table.draw();
    });

    $('#select_staff').on('change', function() {
        filterStaff = $(this).find('option:selected').val();
        table.draw();
    });

    $('#vessalAndVoyageNo').on('change', function() {
        filterVessal = $(this).find('option:selected').val();
        table.draw();
    });

    var availableStockPortFilterELe = $('#port-filter-for-shipping');
    $('#country-filter-available-for-shipping').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        filterCountry = $(this).find('option:selected').val();
        availableStockPortFilterELe.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countryJson, "country", val);
            if (data != null) {
                availableStockPortFilterELe.select2({
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
        }
        table.draw();
    });

    availableStockPortFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        filterPort = $(this).find('option:selected').val();
        table.draw();
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Customer filter
        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[17].length == 0 || aData[17] != filterCustomer) {
                return false;
            }
        }
        //Consignee filter
        if (typeof filterConsignee != 'undefined' && filterConsignee.length != '') {
            if (aData[18].length == 0 || aData[18] != filterConsignee) {
                return false;
            }
        }
        //Staff filter
        if (typeof filterStaff != 'undefined' && filterStaff.length != '') {
            if (aData[19].length == 0 || aData[19] != filterStaff) {
                return false;
            }
        }
        //Destination filter
        if (typeof filterCountry != 'undefined' && filterCountry.length != '') {
            if (aData[20].length == 0 || aData[20] != filterCountry) {
                return false;
            }
        }
        //Destination Port filter
        if (typeof filterPort != 'undefined' && filterPort.length != '') {
            if (aData[4].length == 0 || aData[4] != filterPort) {
                return false;
            }
        }
        //Vessal filter
        if (typeof filterVessal != 'undefined' && filterVessal.length != '') {
            if (aData[6].length == 0 || aData[6] != filterVessal) {
                return false;
            }
        }
        //Due Date Filter
        if (typeof etdDate != 'undefined' && etdDate.length != '') {
            if (aData[9].length == 0 || aData[9] != etdDate) {
                return false;
            }
        }

        //Invoice Date Filter
        if (typeof etaDate != 'undefined' && etaDate.length != '') {
            if (aData[10].length == 0 || aData[10] != etaDate) {
                return false;
            }
        }
        return true;
    });

    $('#consignee').select2({
        allowClear: true,
        width: '100%',
    });
    var targetElement;
    var updateCrAmendEle = $('#modal-update-cr-amend');
    $(updateCrAmendEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        targetElement = $(event.relatedTarget);

        if ($(event.relatedTarget).attr('value') == "single-update") {
            $(updateCrAmendEle).find('button.single-update').removeClass('hidden');
        } else {
            $(updateCrAmendEle).find('button.multiple-update').removeClass('hidden');

            var validDataSelection = [];

            table.rows({
                selected: true,
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var rowData = table.row(this).data();
                if (rowData.recSurStatus == 2) {
                    alert($.i18n.prop('page.bl.amend.uncheck.surrender.status'));
                    event.preventDefault();
                }
                validDataSelection.push(rowData.showBlAmendBtn)
            })

            if (validDataSelection.length == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return false;
            }

            var isValid = isValidSelection(validDataSelection);
            if (!isValid) {
                alert($.i18n.prop('page.accounts.bl.amend'))
                return false;
            }
        }

        // var rowData = table.row($(targetElement).closest('tr')).data();

    }).on('hidden.bs.modal', function() {
        $(this).find('select').val('').trigger('change');
        $(updateCrAmendEle).find('button.single-update, button.multiple-update').addClass('hidden')
    }).on('change', 'select[name="customer"]', function() {
        var consignee = $(this).find('option:selected').data('data');
        var data = consignee.data
        $(updateCrAmendEle).find('#consignee').empty();
        if (!isEmpty(data)) {
            let consArray = data.consigneeNotifyparties.filter(function(index) {
                return !isEmpty(index.cFirstName);
            })
            $('#consignee').select2({
                allowClear: true,
                width: '100%',
                data: $.map(consArray, function(item) {
                    return {
                        id: item.id,
                        text: item.cFirstName
                    };
                })
            }).val('').trigger('change');
        }

    }).on("click", '#update-single-cr', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        var rowData = table.row($(targetElement).closest('tr')).data();
        let blData = getFormData($(updateCrAmendEle).find('select'));
        let data = {};
        data.shippingInstructionId = rowData.shippingInstructionId;
        data.customerId = blData.customer;
        data.consigneeId = blData.consignee;
        response = updateSingleCrAMend(data);
        if (response.status == "success") {
            $(updateCrAmendEle).modal('toggle')
            table.ajax.reload()
        }
    }).on("click", '#update-multiple-cr', function() {
        var arrayOfObject = [];

        let blData = getFormData($('select'));
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let object = {};
            var rowData = table.row(this).data();
            object.shippingInstructionId = rowData.shippingInstructionId;
            object.customerId = blData.customer;
            object.consigneeId = blData.consignee;
            arrayOfObject.push(object);
        })

        response = updateMultipleCrAMend(arrayOfObject);
        if (response.status == "success") {
            $(updateCrAmendEle).modal('toggle')
            table.ajax.reload()
        }
    })

    var blNoTargetElement;
    var updateBlNoEle = $('#modal-update-bl-no');
    $(updateBlNoEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        blNoTargetElement = $(event.relatedTarget);

        //var rowData = table.row($(targetElement).closest('tr')).data();
        if ($(event.relatedTarget).attr('value') == "single-update-bl-no") {
            $(updateBlNoEle).find('button.single-update-bl-no').removeClass('hidden');
        } else {
            $(updateBlNoEle).find('button.multiple-update-bl-no').removeClass('hidden');
            var validDataSelection = [];

            table.rows({
                selected: true,
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var rowData = table.row(this).data();
                if (rowData.recSurStatus == 2) {
                    alert($.i18n.prop('page.bl.amend.uncheck.surrender.status'));
                    event.preventDefault();
                }
                validDataSelection.push(rowData.showBlAmendBtn)
            })

            if (validDataSelection.length == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return false;
            }

            var isValid = isValidSelection(validDataSelection);
            if (!isValid) {
                alert($.i18n.prop('page.accounts.bl.amend'))
                return false;
            }
        }
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('');
        $(updateBlNoEle).find('button.single-update-bl-no, button.multiple-update-bl-no').addClass('hidden')
    }).on("click", '#update-single-bl-no', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        var rowData = table.row($(blNoTargetElement).closest('tr')).data();
        let blData = getFormData($('input'));
        let data = {};
        data.blNo = blData.blNo;
        data.shipmentRequestId = rowData.shipmentRequestId;
        response = updateSingleBlNo(data);
        if (response.status == "success") {
            $(updateBlNoEle).modal('toggle')
            table.ajax.reload()
        }
    }).on("click", '#update-multiple-bl-no', function() {
        var arrayOfObject = [];
        var validDataSelection = [];
        let blData = getFormData($('input'));
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let object = {};
            var rowData = table.row(this).data();
            object.blNo = blData.blNo;
            object.shipmentRequestId = rowData.shipmentRequestId;
            arrayOfObject.push(object);
            validDataSelection.push(rowData.showBlAmendBtn)
        })

        if (arrayOfObject.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }

        var isValid = isValidSelection(validDataSelection);
        if (!isValid) {
            alert($.i18n.prop('page.accounts.bl.amend'))
            return false;
        }

        response = updateMultipleBlNo(arrayOfObject);
        if (response.status == "success") {
            $(updateBlNoEle).modal('toggle')
            table.ajax.reload()
        }
    });

    var modalReceiveOrSurrenderEle = $('#modal-update-Rec-Sur-status');
    var modalReceiveOrSurrenderEleTargetEle;
    $(modalReceiveOrSurrenderEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalReceiveOrSurrenderEleTargetEle = $(event.relatedTarget);
        $(modalReceiveOrSurrenderEle).find('select[name="recSurStatus"]').select2({
            allowClear: true,
            width: '100%'
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('select').val('').trigger('change');
    }).on('click', '#update-status', function(event) {
        var rowData = table.row($(modalReceiveOrSurrenderEleTargetEle).closest('tr')).data();
        let data = {};
        data.shipmentRequestId = rowData.shipmentRequestId;
        data.recSurStatus = $(modalReceiveOrSurrenderEle).find('select[name="recSurStatus"]').find('option:selected').val();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accountsBL/update-rec-sur/status",
            contentType: "application/json",
            async: false,
            success: function(data) {
                $(modalReceiveOrSurrenderEle).modal('toggle');
                table.ajax.reload();
            }
        });
    })

    $(updateCrAmendEle).find('select[name="customer"]').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search/all",
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
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')',
                            data: item
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }
    }).on("click", 'a[name="crDocumentStatus_receive"]', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        let data = row.data();
        response = blDocumentStatusUpdateReceive(data.shipmentRequestId);
        if (response.status == "success") {
            table.ajax.reload()
        }
    }).on("click", 'a[name="crDocumentStatus_issue"]', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        let data = row.data();
        response = blDocumentStatusUpdateIssued(data.shipmentRequestId);
        if (response.status == "success") {
            table.ajax.reload()
        }
    }).on("click", 'a[name="crDocumentStatus_dispatch"]', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        let data = row.data();
        response = blDocumentStatusUpdateDispatched(data.shipmentRequestId);
        if (response.status == "success") {
            table.ajax.reload()
        }
    });

    //invoice payments Detail Modal
    var modalCrTransactionListEle = $('#modal-cr-transaction-list')
    var crTransactionDetailsModalBody = modalCrTransactionListEle.find('#crTransactionDetails');
    modalCrTransactionListEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        crTransactionDetailsModalBody.find('input[name="shippingInstructionId"]').val(rowData.shippingInstructionId)
        setTransactionDetailsData(rowData.shippingInstructionId);
        crTransactionDetailsModalBody.find('input[name="rowData"]').val(JSON.stringify(rowData));
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#crTransactionDetails').find('table').dataTable().fnDestroy();
    });

})

function regex_escape(text) {
    return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
}

function updateSingleCrAMend(data) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        url: myContextPath + "/accountsBL/update-single/crAmend",
        contentType: "application/json",
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function updateMultipleCrAMend(arrayOfObject) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(arrayOfObject),
        url: myContextPath + "/accountsBL/update-multiple/crAmend",
        contentType: "application/json",
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function updateSingleBlNo(data) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        url: myContextPath + "/accountsBL/update-single/blNo",
        contentType: "application/json",
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function updateMultipleBlNo(arrayOfObject) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(arrayOfObject),
        url: myContextPath + "/accountsBL/update-multiple/blNo",
        contentType: "application/json",
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function isValidSelection(data) {
    var tmpPData = '';
    let checkData = true;
    let checkBlData = true;
    for (var i = 0; i < data.length; i++) {
        var showBlAmendBtn = data[i];
        if (i == 0) {
            tmpPData = showBlAmendBtn;
        } else if (!(tmpPData == showBlAmendBtn)) {
            checkData = false;
        }

    }
    for (var i = 0; i < data.length; i++) {
        var showBlAmendBtn = data[i];
        if (showBlAmendBtn == 0) {
            checkBlData = false;
        }
    }
    if (checkData && checkBlData) {
        return true;
    } else {
        return false;
    }

}

function setTransactionDetailsData(shippingInstructionId) {
    blTransactionDetailsDetailTable = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": myContextPath + "/accountsBL/list/transaction/data-source?shippingInstructionId=" + shippingInstructionId,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "shippingInstructionId"
        }, {
            targets: 1,
            "width": "120px",
            "data": "customer"
        }, {
            targets: 2,
            "data": "consignee"
        }, {
            targets: 3,
            "data": "createdDate"
        }, {
            targets: 4,
            "data": "createdBy"
        }]

    })
}

function blDocumentStatusUpdateReceive(shipmentRequestId) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accountsBL/update-docStatus/toReceived?shipmentRequestId=" + shipmentRequestId,
        contentType: false,
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function blDocumentStatusUpdateIssued(shipmentRequestId) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accountsBL/update-docStatus/toIssued?shipmentRequestId=" + shipmentRequestId,
        contentType: false,
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}

function blDocumentStatusUpdateDispatched(shipmentRequestId) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accountsBL/update-docStatus/toDispatched?shipmentRequestId=" + shipmentRequestId,
        contentType: false,
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}
