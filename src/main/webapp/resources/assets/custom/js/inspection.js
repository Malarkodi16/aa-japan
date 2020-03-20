var supplierJson, forwardersJson, transportersJson, countryJson, table, arrangedTable;

$(function() {
    let inspectionChassisNoMatchIndexArr = [];
    let inspectionMashoCopyMatchIndexArr = [];
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $('input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $(document).on('ifChecked', '#showMine', function() {
        arrangedTable.ajax.reload()
    }).on('ifUnchecked', '#showMine', function() {
        arrangedTable.ajax.reload()
    })
    $(document).on('ifChecked', '#lastLapVehiclesCheck', function() {
        table.draw();
    }).on('ifUnchecked', '#lastLapVehiclesCheck', function() {
        table.draw();
    })
    var ins_status = $('input[name="inspectionStatus"]:checked').val();
    $('input[type="radio"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 1) {
            arrangedTable.ajax.reload();
        } else if ($(this).val() == 5) {
            arrangedTable.ajax.reload();
        }
    });
    //set status
    setShippingDashboardStatus();

    var radioSchedule = $('#arrange-inspection-modal').find('input[type="radio"][name="inspectionCompanyFlag"].minimal');
    radioSchedule.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $('#arrange-inspection-modal').on('change', 'select[name="inspectionCompany"]', function() {
        var data = $(this).find(':selected').data('data');
        $('#arrange-inspection-modal').find('select[name="inspectionLocation"]').empty();
        if (!isEmpty(data) && !isEmpty(data.data)) {
            $('#arrange-inspection-modal').find('select[name="inspectionLocation"]').select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                data: $.map(data.data.locations, function(item) {
                    return {
                        id: item.locationId,
                        text: item.locationName,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }
    })
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        var ele = $('#modal-inspection select[name="maker"]');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(data, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger("change");

    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        $('#arrange-transport-modal').find('select[name="destinationCountry"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(countryJson, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        })
        $('#item-vechicle-clone').find('select[name="destinationCountry"]').select2('destroy')
    })

    //AutoNumeric
    $('input[name="charge"]').autoNumeric('init');

    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;
        $('#forwarderCode, #forwarderId').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
        $('#forwarderFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/countries.json", function(data) {

        $('.destinationCountryFilter, #destCountry').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {

        $('#inspectionSelect, #inspectionId').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {

        $('#inspectionSelectFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {

        $('#purchasedSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/colors.json", function(data) {

        $('#color').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.color,
                    text: item.color
                };
            })
        }).val('').trigger("change");
    });

    //populate dropdown options

    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        });
        $('#filter-location').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        });
        var newOption = new Option("Others","others",false,false);
        // Append it to the select
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').append(newOption);
        $('#item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').select2('destroy')
    });

    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        categoriesJson = data;
        $('#transport-items').find('select[name="category"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(categoriesJson, function(item) {
                var childrenArr = [];
                $.each(item.subCategories, function(i, val) {
                    childrenArr.push({
                        id: val.name,
                        text: val.name
                    })
                })
                return {
                    text: item.name,
                    children: childrenArr
                };
            })
        })

        $('#item-vechicle-clone').find('select[name="category"]').select2('destroy')
    });
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
    });

    $('#item-vehicle-container').slimScroll({
        start: 'bottom',
        height: ''
    });

    $('#modal-reason').on('show.bs.modal', function(event) {
        var triggerElement = $(event.relatedTarget);
        var triggerrowindex = arrangedTable.row(triggerElement.closest('tr')).index();

        var data = triggerElement.closest('tr').attr('data-json');
        data = JSON.parse(data);
        $(this).find('input[name="data"]').attr('data-json', JSON.stringify(data));

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-reason-submit', function(event) {
        var rowdata = $('#modal-reason input[name="data"]').attr('data-json');
        rowdata = JSON.parse(rowdata);
        var data = {};
        data["reason"] = $('#modal-reason textarea[name="reason"]').val();
        data["stockNo"] = rowdata.stockNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + '/inspection/cancel?code=' + rowdata.inspectionCode,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var index = $('#modal-reason input[name="data"]').attr('data-trigger-row-index');
                    arrangedTable.ajax.reload();
                }
                $('#modal-reason').modal('toggle');
            }
        });

    })

    

    $('.inspection-arranged-filter').addClass('hidden');
    $('#document-data-container').addClass('hidden');
    //Radio Button with Container 
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 2) {
            $('#modal-inspection').find('input[name="certificateNo"]').removeClass('required')
            $('#requiredRemove').addClass('notrequired');
        } else {
            $('#modal-inspection').find('input[name="certificateNo"]').addClass('required')
            $('#requiredRemove').removeClass('notrequired');
        }
        if ($(this).val() == 0) {
            showAvailableStock();
            table.ajax.reload()
        } else if ($(this).val() == 1) {
            arraShippingInstrctionOrInspection();
            tableInstruction.ajax.reload()
        } else if ($(this).val() == 2) {
            showArrangedStock();
            arrangedTable.ajax.reload()
            //       setDatatableColumns(arrangedTable, [0, 1, 2, 3, 4, 6, 7, 10])
        } else if ($(this).val() == 3) {
            showCompletedStock();
            tableCompleted.ajax.reload();
            // setDatatableColumns(arrangedTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9])
        } else if ($(this).val() == 4) {
            showCancelledOrders();
            tableCancelled.ajax.reload();
            // setDatatableColumns(arrangedTable, [0, 1, 2, 3, 4, 6, 7, 11])
        }
    });

    //DatePicker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })
    //
    $('.documentDatepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        allowClear: true
    })
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    //Available Vehicle Table
    table = $('#table-inspection').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 100,
        'ordering': true,
        "ajax": {
            url: myContextPath + "/inspection/inspection-data",
            beforeSend: function() {
                $('div#avail-vehicle-container>div.overlay').show()
            },
            complete: function() {
                $('div#avail-vehicle-container>div.overlay').hide();
            }
        },
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
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 2,
            "data": "model"
        }, {
            targets: 3,
            "data": "destinationCountry"

        }, {
            targets: 4,
            "data": "bookingDetails"
        }, {
            targets: 5,
            "data": "firstRegDate"
        }, {
            targets: 6,
            "data": "color"
        }, {
            targets: 7,
            "data": "lastTransportLocation",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.lastTransportLocationCustom
                } else {
                    return row.sLastTransportLocation;
                }
            }
        }, {
            targets: 8,
            "data": "supplierName"
        }, {
            targets: 9,
            "data": "lotNo"
        }, {
            targets: 10,
            "data": "isPhotoUploaded",
            "render": function(data, type, row) {
                var status = 'PHOTO_NOT_RECEIVED';
                var className = "label-default";
                if (data == 0) {
                    status = 'NO PHOTO';
                    className = "label-default";
                } else if (data == 1) {
                    status = 'PHOTO OK';
                    className = "label-success";
                }
                return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'

            }
        }, {
            targets: 11,
            "data": "documentReceivedDate"
        }, {
            targets: 12,
            "data": "etd",
            visible: false
        }, {
            targets: 13,
            "data": "shippingInstructionStatus",
            "render": function(data, type, row) {
                let text = '';
                if (!isEmpty(data)) {
                    if (data == 0) {
                        text = 'Immediate';
                    } else if (data == 1) {
                        text = 'Next Available';
                    } else if (data == 2) {
                        text = row.estimatedDeparture;
                    }
                }
                return text;
            }
        }, {
            targets: 14,
            "data": "shippingDate"
        }, {
            targets: 15,
            "data": "vessalName"
        }, {
            targets: 16,
            "data": "transporterName"
        }, {
            targets: 17,
            className: 'align-center',
            "data": "transportStatus",
            "render": function(data, type, row) {
                var status;
                var className = "info";
                if (data == "1") {
                    status = "Requested"
                } else if (data == "2") {
                    status = "Confirmed"
                } else if (data == "4") {
                    status = "Rearrangement"
                } else if (data == "5") {
                    status = "Delivery Confirm"
                } else if (data == "6") {
                    status = "Delivered"
                } else if (data == "7") {
                    status = "In Transit"
                } else {
                    status = "Idle"
                }
                if (row.chassisNo == "MK53S-834135") {

                    console.log(status + "" + className)
                }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 18,
            "data": "transportDeliveryDate"
        }, {
            targets: 19,
            "data": "inspectionStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Not Arranged';
                    var className = "label-default";
                    if (data == 0) {
                        status = 'Yet to Arrange';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'Inspection Done';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }

        }, {
            targets: 20,
            "data": "shippingStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Not Arranged';
                    var className = "label-default";
                    if (isEmpty(data)) {
                        var status = 'Not Arranged';
                        var className = "label-default";
                    } else if (data == 0) {
                        status = 'Shipping Arranged';
                        className = "label-warning";
                    } else if (data == 1) {
                        status = 'Shipping Confirmed';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }

        }, {
            targets: 21,
            "data": "lastTransportLocation",
            visible: false
        }, {
            targets: 22,
            "data": "supplier",
            visible: false
        }, {
            targets: 23,
            "data": "purchaseDate",
            visible: false
        }, {
            targets: 24,
            "visible": false,
            "className": "details-control",
            "data": "lastlapStatus"

        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {}

    });
    $(".year-month-picker").inputmask({
        mask: "9999/99"
    });

    //Arrange Inspection Table
    arrangedTable = $('#table-arrange').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 100,
        "aaSorting": [[0, "desc"]],
        "ajax": {
            'url': myContextPath + "/inspection/inspection-data-arranged",
            beforeSend: function() {
                $('div#arrange-vehicle-container>div.overlay').show()
            },
            complete: function() {
                $('div#arrange-vehicle-container>div.overlay').hide();
            },
            'data': function(data) {
                if (!$('input[name="showMine"]').is(':checked')) {
                    data.show = 0;
                } else {
                    data.show = 1;
                }
                if ($('input[name="inspectionStatus"]:checked').val() != 5) {
                    data.showVehicel = 0;
                } else {
                    data.showVehicel = 5;
                }
                return data;

            }
        },

        ordering: true,
        select: {
            style: 'multi',
            selector: 'td:first-child>input',
            width: 50
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var params = 'date=' + row.inspectionDate;
                    params += '&country=' + row.country;
                    if (row.inspectionCompanyFlag == 0) {
                        params += '&inspection_company=' + ifNotValid(row.inspectionCompanyId, '');
                        params += '&location=' + ifNotValid(row.location.locationId, '');
                    } else if (row.inspectionCompanyFlag == 1) {
                        params += '&forwarder=' + ifNotValid(row.forwarderId, '');
                    }
                    var totalItems = row.items.length;
                    var html = '';

                    html += '<a class="ml-5 btn btn btn-primary btn-xs" title="Photo Not Received" name="photoNotReceived" id="photoNotReceived"><i class="fa fa-fw fa-minus"></i>Photo Not Received</a>'

                    html += '<a class="ml-5 btn btn btn-primary btn-xs" title="Photo Received" name="photoReceived" id="photoReceived"><i class="fa fa-fw fa-plus"></i>Photo Received</a>'

                    html += '<a class="ml-5 btn btn btn-primary btn-xs" title="Masho Copy Received" data-toggle="modal" data-target="#inspectionArrangedMashoCopyReceivedModal" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-plus"></i>Masho Copy Received</a>'

                    html += '<a href="#" class="ml-5 btn btn-info btn btn-default btn-xs" data-toggle="modal" data-target="#modal-inspection-document-multiple" data-backdrop="static" data-keyboard="false" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';

                    html += '<a href="#" class="ml-5 btn btn-success btn-xs" title="Pass" name="inspection-passed-multiple"><i class="fa fa-fw fa-check"></i></a>'

                    html += '<a href="' + myContextPath + '/inspection/application?' + params + '&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Inspection Application [PDF]</a>';

                    html += '<a href="' + myContextPath + '/inspection/application?' + params + '&format=xls" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Inspection Application [Excel]</a>';

                    var inspectionCompany = ifNotValid(row.inspectionCompanyFlag, '') == 0 ? ifNotValid(row.inspectionCompany, '') : ifNotValid(row.forwarder, '');

                    return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.country + ' - ' + inspectionCompany + ' - ' + (isEmpty(row.location) ? '' : ifNotValid(row.location.locationName, '')) + ' [InsDate - ' + row.inspectionSentDate + '] - ' + totalItems + '</span></h5></div><div class="action-container pull-right">' + html + '</div</div></div></div>';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "inspectionCompanyId",
            "visible": false
        }, {
            targets: 2,
            "data": "forwarder",
            "visible": false
        }, {
            targets: 3,
            "data": "inspectionCompanyFlag",
            "visible": false
        }, {
            targets: 4,
            "data": "country",
            "visible": false
        }, {
            targets: 5,
            "data": "inspectionSentDate",
            "visible": false
        }, {
            targets: 6,
            data: 'items',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }],
        "drawCallback": function(settings, json) {

            $(settings.nTHead).hide();
            $('input.autonumber,span.autonumber').autoNumeric('init')
            inspectionChassisNoMatchIndexArr = getUnique(inspectionChassisNoMatchIndexArr, 'index');
            inspectionMashoCopyMatchIndexArr = getUnique(inspectionMashoCopyMatchIndexArr, 'index');

            for (let i = 0; i < inspectionChassisNoMatchIndexArr.length; i++) {
                var row = arrangedTable.row(inspectionChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = inspectionChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            inspectionChassisNoMatchIndexArr = []

            for (let i = 0; i < inspectionMashoCopyMatchIndexArr.length; i++) {
                var row = arrangedTable.row(inspectionMashoCopyMatchIndexArr[i]['index']);
                var tr = $(row.node());
                let data = arrangedTable.row(tr).data();
                let matchMashoCopyIdArr = inspectionMashoCopyMatchIndexArr[i]['matchIdArr'];
                data = data.items.filter(function(val) {
                    return matchMashoCopyIdArr.indexOf(val.inspectionId) != -1;
                });
                if (matchMashoCopyIdArr.length > 0) {
                    var detailsElement = format1(data);
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                }
            }
            inspectionMashoCopyMatchIndexArr = []

        }
    })
    arrangedTable.on('click', 'button[name="inspection-passed"]', function() {
        let data = $(this).closest('tr').attr('data-json');
        data = JSON.parse(data);
        let inspectionId = data.inspectionId;
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            //             data: JSON.stringify(instructionData),
            url: myContextPath + "/inspection/pass?inspectionId=" + inspectionId,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload()
                }
            }
        })
    })

    arrangedTable.on('click', 'a[name="inspection-passed-multiple"]', function(e) {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        inspectionData = arrangedTable.row($(this).closest('tr')).data();

        let items = [];
        let flag = false;
        var row = arrangedTable.row($(this).closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            }

        }

        if (items.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false
        }
        items.forEach(function(item) {
            if (item.transportationStatus != 2) {
                flag = true;
            }
        })
        if (flag) {
            alert("Please Select Transport Completed Vehicles");
            return false
        }
        var inspectionIds = [];
        items.forEach(function(data) {
            inspectionIds.push(data.inspectionId);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(inspectionIds),
            url: myContextPath + "/inspection/pass-multiple",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload()
                }
            }
        })

    })

    arrangedTable.on('click', 'a[name="inspection-delete"]', function() {
        let data = $(this).closest('tr').attr('data-json');
        data = JSON.parse(data);
        let inspectionId = data.inspectionId;
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "delete",
            //             data: JSON.stringify(instructionData),
            url: myContextPath + "/inspection/delete?inspectionId=" + inspectionId,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload()
                }
            }
        })

    })
    arrangedTable.on('click', 'a[name="photoNotReceived"]', function(e) {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        inspectionData = arrangedTable.row($(this).closest('tr')).data();

        let items = [];
        var row = arrangedTable.row($(this).closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                //                 let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                //                 $.each(selectedRow, function(item) {
                //                     let data = $(this).closest('tr').attr('data-json');
                //                     data = JSON.parse(data);
                //                     items.push(data);
                //                 })
                alert($.i18n.prop('common.alert.stock.noselection'));
                return e.preventDefault();
            }

        } else {
            showDetails($(this).closest('tr'));

            var row = arrangedTable.row($(this).closest('tr'));
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }
        var data_stock = [];
        items.forEach(function(data) {
            let rowData = {};
            rowData['stockNo'] = data.stockNo;
            rowData['chassisNo'] = data.chassisNo;
            data_stock.push(rowData);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_stock),
            url: myContextPath + "/inspection/stock/update/photoNotReceived",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                }
            }
        })

    })
    arrangedTable.on('click', 'a[name="photoReceived"]', function(e) {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        inspectionData = arrangedTable.row($(this).closest('tr')).data();

        let items = [];
        var row = arrangedTable.row($(this).closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                //                 let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                //                 $.each(selectedRow, function(item) {
                //                     let data = $(this).closest('tr').attr('data-json');
                //                     data = JSON.parse(data);
                //                     items.push(data);
                //                 })
                alert($.i18n.prop('common.alert.stock.noselection'));
                return e.preventDefault();
            }

        } else {
            showDetails($(this).closest('tr'));

            var row = arrangedTable.row($(this).closest('tr'));
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }
        var data_stock = [];
        items.forEach(function(data) {
            let rowData = {};
            rowData['stockNo'] = data.stockNo;
            rowData['chassisNo'] = data.chassisNo;
            data_stock.push(rowData);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_stock),
            url: myContextPath + "/inspection/stock/update/photoReceived",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                }
            }
        })

    })
    var targetElement;
    $('div#inspectionArrangedMashoCopyReceivedModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

        targetElement = $(event.relatedTarget);

        $('#inspectionArrangedMashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').datepicker('setDate', new Date())

    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
    }).on('click', 'button#save', function() {

        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        if (!$('div#inspectionArrangedMashoCopyReceivedModal form#form-mashoCopyReceivedModal').valid()) {
            return false;
        }
        inspectionData = arrangedTable.row($(targetElement).closest('tr')).data();
        let items = [];
        var row = arrangedTable.row($(targetElement).closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })
            }

        } else {
            showDetails($(targetElement).closest('tr'));

            var row = arrangedTable.row($(targetElement).closest('tr'));
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }
        let date = $('div#inspectionArrangedMashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').val();
        var data_stock = [];
        items.forEach(function(data) {
            let rowData = {};
            rowData['stockNo'] = data.stockNo;
            rowData['chassisNo'] = data.chassisNo;
            data_stock.push(rowData);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_stock),
            url: myContextPath + "/inspection/stock/update/mashoCopyReceived?date=" + date,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                    $('div#inspectionArrangedMashoCopyReceivedModal').modal('toggle');
                }
            }
        })
    })

    let inspectionMultipleUpdateModalEle;
    $('#modal-inspection-document-multiple').on('show.bs.modal', function(event) {

        if (event.namespace != 'bs.modal') {
            return;
        }
        inspectionMultipleUpdateModalEle = $(event.relatedTarget);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('select').val('').trigger('change');
    }).on('click', '#document-save-multiple-data', function() {
        if (!$('#form-inspection-document-multiple').find('input,select').valid()) {
            return;
        }

        inspectionData = arrangedTable.row($(inspectionMultipleUpdateModalEle).closest('tr')).data();

        let items = [];
        var row = arrangedTable.row($(inspectionMultipleUpdateModalEle).closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })
            }

        } else {
            showDetails($(inspectionMultipleUpdateModalEle).closest('tr'));

            var row = arrangedTable.row($(inspectionMultipleUpdateModalEle).closest('tr'));
            let selectedRow = row.child().find('div.inspection-order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }
        var data_inspection = [];
        var modaInspectionDocumentMultiple = $('#modal-inspection-document-multiple')
        var object = getFormData(modaInspectionDocumentMultiple.find('input,select'));
        items.forEach(function(data) {
            let rowData = {};
            rowData["date"] = object.documentSentDate;
            rowData["status"] = object.inspectionStatus;
            rowData['id'] = data.inspectionId;
            data_inspection.push(rowData);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_inspection),
            url: myContextPath + "/inspection/update-document-multiple",
            contentType: "application/json",
            success: function(data) {
                console.log(data);
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                }
                //set status
                setShippingDashboardStatus();
                $(modaInspectionDocumentMultiple).modal('toggle');
            }
        })

    });

    //Available Vehicle Table CheckBox
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
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    });
    //Arrange Vehicle Table CheckBox
    arrangedTable.on('click', 'td>.container-fluid>.row>.col-md-12>div.details-control', function() {
        var tr = $(this).closest('tr');
        var row = arrangedTable.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            //             table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            //                 var row = table.row(rowIdx);
            //                 if (row.child.isShown()) {
            //                     row.child.hide();
            //                     $(row.node()).removeClass('shown');
            //                     $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            //                 }

            //             })
            //             tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            //             var detailsElement = format(row.data());
            //             row.child(detailsElement).show();
            //             detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            //             tr.addClass('shown');
            showDetails(tr);
        }
    });
    function showDetails(tr) {
        // var tr = $(this).closest('tr');
        var row = arrangedTable.row(tr)
        arrangedTable.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }
        })
        //row.child(format(row.data())).show();
        var detailsElement = format(row.data());
        row.child(detailsElement).show();
        detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
        tr.addClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        arrangedTable.draw();

    }
    arrangedTable.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            arrangedTable.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            arrangedTable.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            arrangedTable.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            arrangedTable.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (arrangedTable.rows({
            selected: true,
            page: 'current'
        }).count() !== arrangedTable.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (arrangedTable.rows({
            selected: true,
            page: 'current'
        }).count() !== arrangedTable.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    });
    //Table Global Search
    $('#table-filter-search').keyup(function() {
        //for available vehicle table
        table.search($(this).val()).draw();
        //for arrange vehicle table
        arrangedTable.search($(this).val()).draw();
        // for inspection-completed-table
        tableCompleted.search($(this).val()).draw();
        // for inspection Requested table
        tableInstruction.search($(this).val()).draw();
        // for inspection-cancelled-table
        tableCancelled.search($(this).val()).draw();

    });
    //Table Length
    $('#table-filter-length').change(function() {
        //for available vehicle table
        table.page.len($(this).val()).draw();
        //for arrange vehicle table
        arrangedTable.page.len($(this).val()).draw();
        //for inspection-completed vehicle table
        tableCompleted.page.len($(this).val()).draw();
        // for inspection Requested table
        tableInstruction.page.len($(this).val()).draw();
        // for inspection-cancelled-table
        tableCancelled.page.len($(this).val()).draw();

    });

    //Arrange Inspection On Click event
    $('#arrange-inspection').on('click', function(event) {
        event.preventDefault();

    })
    // status update on photo button
    $('#upload-photo-btn').on('click', function(event) {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }

        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }

        var data_stock = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let rowData = {};
            var data = table.row(this).data();
            rowData['stockNo'] = data.stockNo;
            rowData['chassisNo'] = data.chassisNo;
            data_stock.push(rowData);
        })

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_stock),
            url: myContextPath + "/inspection/stock/update/photoReceived",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                }
            }
        })
    })

    // status update on photo button
    $('div#mashoCopyReceivedModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (table.rows({
            selected: true,
            page: 'all'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }

        $('#mashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').datepicker('setDate', new Date())

    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
    }).on('click', 'button#save', function() {

        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        if (!$('div#mashoCopyReceivedModal form#form-mashoCopyReceivedModal').valid()) {
            return false;
        }
        let date = $('div#mashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').val();
        var data_stock = [];
        table.rows({
            selected: true,
            page: 'all'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let rowData = {};
            var data = table.row(this).data();
            rowData['stockNo'] = data.stockNo;
            rowData['chassisNo'] = data.chassisNo;
            data_stock.push(rowData);
        })

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data_stock),
            url: myContextPath + "/inspection/stock/update/mashoCopyReceived?date=" + date,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    $('div#mashoCopyReceivedModal').modal('toggle');
                }
            }
        })
    })
    var inspectionInstructionEle = $('#arrange-instruction-modal');
    var inspectionModalEle;
    inspectionInstructionEle.on('show.bs.modal', function(event) {

        if (event.namespace != 'bs.modal') {
            return;
        }
        inspectionModalEle = $(event.relatedTarget);
        let destCountry = "";
        if (!isEmpty(inspectionModalEle) && inspectionModalEle[0].id === "arrange-sec-instruction-btn") {

            if (tableCompleted.rows({
                selected: true,
                page: 'all'
            }).count() == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return event.preventDefault();
            }
            destCountry = $('#filter-destCountry-second-instruction').val()
            if (isEmpty(destCountry)) {
                alert($.i18n.prop('alert.instruction.select.destCountry'));
                return event.preventDefault();
            }
        } else {

            if (table.rows({
                selected: true,
                page: 'all'
            }).count() == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return event.preventDefault();
            }
            destCountry = $('#filter-destination-country').val()
            if (isEmpty(destCountry)) {
                alert($.i18n.prop('alert.instruction.select.destCountry'));
                return event.preventDefault();
            }
        }

        inspectionInstructionEle.find('select[name="destCountry"]').val(destCountry).trigger('change');
    }).on('hidden.bs.modal', function() {
        $(this).find("input:not(input[type='radio']),select").val('').end();
        $(this).find('select').val('').trigger('change');
    }).on('click', '#instruction-submit', function() {
        var data_stock = [];
        if (!$('#form-arrange-inspection-instruction').valid()) {
            return false;
        }

        if (!isEmpty(inspectionModalEle) && inspectionModalEle[0].id === "arrange-sec-instruction-btn") {

            tableCompleted.rows({
                selected: true,
                page: 'all'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var data = tableCompleted.row(this).data();
                data_stock.push(data.stockNo);
            })

        } else {

            table.rows({
                selected: true,
                page: 'all'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var data = table.row(this).data();
                data_stock.push(data.stockNo);
            })
        }
        let destCountry = inspectionInstructionEle.find('select[name="destCountry"]').val();
        var instructionData = [];
        for (var i = 0; i < data_stock.length; i++) {
            data = {};
            data.stockNo = data_stock[i];
            data.destCountry = destCountry;
            instructionData.push(data);
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(instructionData),
            url: myContextPath + "/inspection/instruction/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload()
                    tableCompleted.ajax.reload()
                    tableInstruction.ajax.reload();
                    var alertEle = $('#alert-block');
                    //set status
                    setShippingDashboardStatus();

                    $('#arrange-instruction-modal').modal('toggle');
                }
            }
        })

    });

    $('#type').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        if (selectedVal == '0') {
            $('#inspection-company').removeClass('hidden');
            $('#forwarder-company').val('').trigger('change').addClass('hidden')
        } else if (selectedVal == '1') {
            $('#inspection-company').val('').trigger('change').addClass('hidden')
            $('#forwarder-company').removeClass('hidden');
        } else {
            $('#inspection-company').val('').trigger('change').addClass('hidden')
            $('#forwarder-company').val('').trigger('change').addClass('hidden')
        }
        arrangedTable.draw();
    });

    //Arranged Vehicle Document Update
    var inspectionId;
    $('#modal-inspection-document').on('show.bs.modal', function(event) {

        if (event.namespace != 'bs.modal') {
            return;
        }
        inspectionModalEle = $(event.relatedTarget);
        var rowData = $(inspectionModalEle).closest('tr').attr('data-json');
        rowData = JSON.parse(rowData);
        $(this).find('input[name="id"]').val(rowData.inspectionId)
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('select').val('').trigger('change');
    }).on('click', '#document-save-data', function() {
        if (!$('#form-modal-inspection-document').find('input,select').valid()) {
            return;
        }
        var modaInspectionDocument = $('#modal-inspection-document')
        var object = getFormData(modaInspectionDocument.find('input,select,textarea'));
        var inspectionOrderDocument = {};

        inspectionOrderDocument["date"] = object.documentSentDate;
        inspectionOrderDocument["status"] = object.inspectionStatus;
        inspectionOrderDocument["id"] = $('#modal-inspection-document').find('input[name="id"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(inspectionOrderDocument),
            url: myContextPath + "/inspection/update-document",
            contentType: "application/json",
            success: function(data) {
                console.log(data);
                if (data.status === 'success') {
                    //                     var tr = $(inspectionModalEle).closest('tr');

                    arrangedTable.ajax.reload();
                    //                     row.data(data.data.updatedData).invalidate();
                }
                //set status
                setShippingDashboardStatus();
                $(modaInspectionDocument).modal('toggle');
            }
        })

    });

    //Arranged Vehicle Inspection Date and EngineNo Update
    var stockNo
    $('#modal-inspection').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData;
        inspectionModalEle = $(event.relatedTarget);
        if ($(inspectionModalEle).attr('data-flag') == 0) {
            rowData = $(inspectionModalEle).closest('tr').attr('data-json');
            rowData = JSON.parse(rowData);
            //             $(this).find('#rowData').attr('data-json', JSON.stringify(rowData));
        } else if ($(inspectionModalEle).attr('data-flag') == 1) {
            var tr = $(inspectionModalEle).closest('tr');
            rowData = tableCompleted.row(tr).data();
            //             $(this).find('#rowData').attr('data-json', JSON.stringify(rowData));
        }

        $(this).find('#rowData').attr('data-json', JSON.stringify(rowData));
        $(this).find('input[name="dateOfIssue"]').val(ifNotValid(rowData.dateOfIssue));
        $(this).find('input[name="certificateNo"]').val(ifNotValid(rowData.certificateNo));
        $(this).find('input[name="engineNo"]').val(ifNotValid(rowData.engineNo));
        $(this).find('input[name="chassisNo"]').val(ifNotValid(rowData.chassisNo));
        $(this).find('#color').val(rowData.color).trigger('change');
        $(this).find('input[name="cc"]').val(ifNotValid(rowData.cc));
        $(this).find('input[name="firstRegDate"]').val(ifNotValid(rowData.sFirstRegDate));
        $(this).find('input[name="mileage"]').val(ifNotValid(rowData.mileage));
        $(this).find('textarea[name="remarks"]').val(ifNotValid(rowData.remarks));
        $(this).find('select[name="maker"]').val(ifNotValid(rowData.maker)).trigger('change');
        $(this).find('select[name="model"]').val(ifNotValid(rowData.model)).trigger('change');
        $(this).find('input[name="chassisNo"]').attr('readonly', true)

        for (var i = 0; i < rowData.equipments.length; i++) {
            $(this).find('input[name="equipment"][value="' + rowData.equipments[i] + '"]').attr('checked', true);
        }

    }).on('hidden.bs.modal', function() {
        $(this).find(".reset-on-close").val('');
        $(this).find('select.select2').val('').trigger('change');
    }).on('click', '#inspection-details-save', function() {
        if (!$('#form-inspection-details').valid()) {
            return false;
        }
        var rowData = JSON.parse($('#modal-inspection').find('#rowData').attr('data-json'));
        var modaInspection = $('#modal-inspection')
        var stock = getFormData(modaInspection.find('.stock-details-to-save.object'));
        var inspection = getFormData(modaInspection.find('.inspection-save-data'));
        var equip = [];
        var equipEle = $('.stock-details-to-save.array');
        for (var i = 0; i < equipEle.length; i++) {
            if (equipEle[i].checked) {
                var equipment = $(equipEle)[i].value;
                equip.push(equipment)
            }
        }
        var inspectionOrderDocument = {};
        inspectionOrderDocument["certificateNo"] = inspection.certificateNo;
        inspectionOrderDocument["dateOfIssue"] = inspection.dateOfIssue;

        inspectionOrderDocument["ids"] = ifNotValid(rowData.inspectionId);
        inspectionOrderDocument["stockNo"] = ifNotValid(rowData.stockNo);
        inspectionOrderDocument["country"] = ifNotValid(rowData.country);
        inspectionOrderDocument["equipment"] = equip;
        inspectionOrderDocument["chassisNo"] = stock.chassisNo;
        inspectionOrderDocument["color"] = stock.color;
        inspectionOrderDocument["engineNo"] = stock.engineNo
        inspectionOrderDocument["maker"] = stock.maker;
        inspectionOrderDocument["model"] = stock.model;
        inspectionOrderDocument["sFirstRegDate"] = stock.firstRegDate;
        inspectionOrderDocument["cc"] = stock.cc;
        inspectionOrderDocument["remarks"] = stock.remarks;
        inspectionOrderDocument["hsCode"] = stock.hsCode;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(inspectionOrderDocument),
            url: myContextPath + "/inspection/update-inspection-info",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                    var alertEle = $('#alert-block');
                    $(alertEle).css('display', '').html('<strong>Success!</strong> Inspection successfully completed.').fadeTo(2000, 500).slideUp(500, function() {
                        $(alertEle).slideUp(500);
                    });
                    //set status
                    setShippingDashboardStatus();
                    $(modaInspection).modal('toggle');
                }

            }
        })

    }).on('change', 'select[name="maker"]', function() {
        var data = $(this).find(':selected').data('data').data;
        var modelEle = $('#modal-inspection select[name="model"]');
        $(modelEle).empty();
        if (!isEmpty(data)) {
            var modelList = data.models;
            $(modelEle).select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName + ' -> ' + item.subcategory,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }

    });
    ;//Date Range
    var purchased_min;
    var purchased_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    });
    //Filter Data
    var supplierName;
    $('#purchasedSupplier').change(function() {
        supplierName = $(this).val();
        table.draw();
        //table Draw
    })
    $('#filter-destination-country').change(function() {
        table.draw();
    })
    $('#filter-destination-country-instruction').change(function() {
        tableInstruction.draw();
    })
    $('#filter-location').change(function() {
        table.draw();
    })
    var forwarderName;
    $('#forwarderFilter').change(function() {
        forwarderName = $(this).val();
        arrangedTable.draw();
        //table Draw
    })
    var inspectionCompanyName;
    $('#inspectionSelectFilter').change(function() {
        inspectionCompanyName = $(this).val();
        arrangedTable.draw();
        //table Draw
    })

    var mashoCopyDateFilter;
    $('#table-filter-masho-copy-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        mashoCopyDateFilter = $(this).val();
        $(this).closest('.input-group').find('.masho-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon masho-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        arrangedTable.draw();
    });
    $('.input-group').on('click', '.masho-clear-date', function() {
        mashoCopyDateFilter = '';
        $('#table-filter-masho-copy-date').val('');
        $(this).remove();
        arrangedTable.draw();

    })

    var mashoCopyDateFilterArrange;
    $('#table-filter-masho-copy-date-available').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        mashoCopyDateFilterArrange = $(this).val();
        $(this).closest('.input-group').find('.masho-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon masho-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });

    $('.input-group').on('click', '.masho-clear-date', function() {
        mashoCopyDateFilterArrange = '';
        $('#table-filter-masho-copy-date-available').val('');
        $(this).remove();
        table.draw();

    })

    var status;
    $('#statusFilter').change(function() {
        status = $(this).val();
        arrangedTable.draw();
        //table Draw
    })
    var childDetails = [];

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        //console.log("inside :: fn.dataTable.ext.search.push")
        if (settings.sTableId == 'table-arrange') {
            var term = $('#table-filter-search').val().toLowerCase();
            var receivedDate = $('#table-filter-masho-copy-date').val().toLowerCase();
            if (isEmpty(term) && isEmpty(receivedDate)) {
                return true;
            }
            var orderItem = JSON.parse(data[6]);
            let highlightArr = [];
            let highlightMashoCopyArr = [];
            let isFound = false;
            let ismashoCopyFound = false;
            var row = arrangedTable.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            for (var i = 0; i < orderItem.length; i++) {

                if (!isEmpty(term)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(term) != -1) {
                        highlightArr.push(orderItem[i].inspectionId);
                        isFound = true;

                    } else if (ifNotValid(orderItem[i]["model"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["color"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["sLastTransportLocation"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["supplierName"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["bookingDetails"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["transporterName"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["inspectionCompany"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    } else if (ifNotValid(orderItem[i]["forwarder"], '').toLowerCase().indexOf(term) != -1) {
                        return true;
                    }

                }

                if (!isEmpty(receivedDate)) {
                    if (ifNotValid(orderItem[i]["mashoCopyReceivedDate"], '').toLowerCase().indexOf(receivedDate) != -1) {
                        highlightMashoCopyArr.push(orderItem[i].inspectionId);
                        ismashoCopyFound = true;
                    } else {
                        ismashoCopyFound = false;
                    }
                }
                //                 if (inspection_status == 5) {
                //                     if (~ifNotValid(orderItem[i]["status"].toString(), '').indexOf(inspection_status))
                //                         return true;
                //                 }
                //                 if (isEmpty(term) &&isEmpty(receivedDate)&& inspection_status == 1) {
                //                     return true
                //                 }

            }
            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                inspectionChassisNoMatchIndexArr.push(object);
                return true
            }
            if (ismashoCopyFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightMashoCopyArr;
                inspectionMashoCopyMatchIndexArr.push(object);
                ismashoCopyFound = false;
                return true
            }
            return false;

        } else {
            return true;
        }
        return false;
    })

    // Date range picker
    var inspection_min;
    var inspection_max;
    $('#table-filter-inspection-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        inspection_min = picker.startDate;
        inspection_max = picker.endDate;
        picker.element.val(inspection_min.format('DD-MM-YYYY') + ' - ' + inspection_max.format('DD-MM-YYYY'));
        inspection_min = inspection_min._d.getTime();
        inspection_max = inspection_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        tableCompleted.draw();
    });
    $('#date-form-group-inspection').on('click', '.clear-date', function() {
        inspection_min = '';
        inspection_max = '';
        tableCompleted.draw();
        $('#table-filter-inspection-date').val('');
        $(this).remove();

    })

    var filterSupplierName = $('#purchasedSupplier').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == 'table-inspection') {
            let lastLapVehiclesCheck = ifNotValid($('#lastLapVehiclesCheck').is(':checked'), '');
            //date filter
            if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
                if (aData[23].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[23], 'DD-MM-YYYY')._d.getTime();
                }
                if (purchased_min && !isNaN(purchased_min)) {
                    if (aData._date < purchased_min) {
                        return false;
                    }
                }
                if (purchased_max && !isNaN(purchased_max)) {
                    if (aData._date > purchased_max) {
                        return false;
                    }
                }

            }
            //Supplier filter
            if (typeof supplierName != 'undefined' && supplierName.length != '') {
                if (aData[22].length == 0 || aData[22] != supplierName) {
                    return false;
                }
            }
            //Destination country filter
            var country = $('#filter-destination-country').val();
            if (!isEmpty(country)) {
                if (aData[3].length == 0 || aData[3] != country) {
                    return false;
                }
            }
            var mashoCopyReceivedArranged = $('#table-filter-masho-copy-date-available').val();
            if (!isEmpty(mashoCopyReceivedArranged)) {
                if (aData[11].length == 0 || aData[11] != mashoCopyReceivedArranged) {
                    return false;
                }
            }
            //Current location
            var location = $('#filter-location').val();
            if (!isEmpty(location) && location.length > 0) {
                if (aData[21].length == 0 || location.indexOf(aData[21]) == -1) {
                    return false;
                }
            }

            if (typeof lastLapVehiclesCheck != 'undefined' && lastLapVehiclesCheck == true) {
                if (ifNotValid(aData[24], '0') == '0') {
                    return false;
                }
            }
        }
        if (oSettings.sTableId == 'table-inspection-instruction') {
            var country = $('#filter-destination-country-instruction').val();
            if (!isEmpty(country)) {
                if (aData[6].length == 0 || aData[6] != country) {
                    return false;
                }
            }
        }
        if (oSettings.sTableId == 'table-arrange') {
            var inspection_status = $('input[name="inspectionStatus"]:checked').val();
            //forwarder filter
            var type = $('#type').val();
            if (typeof type != 'undefined' && type != null && type.length != '') {
                if (aData[3].length == 0 || aData[3] != type) {
                    return false;
                }
            }
            if (type == 0) {
                if (typeof inspectionCompanyName != 'undefined' && inspectionCompanyName != null && inspectionCompanyName.length != '') {
                    if (aData[1].length == 0 || aData[1] != inspectionCompanyName) {
                        return false;
                    }
                }
            } else if (type == 1) {
                if (typeof forwarderName != 'undefined' && forwarderName != null && forwarderName.length != '') {
                    if (aData[2].length == 0 || aData[2] != forwarderName) {
                        return false;
                    }
                }
            }

        }

        if (oSettings.sTableId == 'table-completed') {
            var country = $('#filter-destCountry-second-instruction').val();
            if (!isEmpty(country)) {
                if (aData[7].length == 0 || aData[7] != country) {
                    return false;
                }
            }

            // date filter
            if (typeof inspection_min != 'undefined' && inspection_max.length != '') {
                if (aData[17].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[17], 'DD/MM/YYYY')._d.getTime();
                }
                if (inspection_min && !isNaN(inspection_min)) {
                    if (aData._date < inspection_min) {
                        return false;
                    }
                }
                if (inspection_max && !isNaN(inspection_max)) {
                    if (aData._date > inspection_max) {
                        return false;
                    }
                }

            }
        }
        return true;
    });

    //Arrange Transport Modal
    var radioSchedule = $('#arrange-transport-modal').find('input[type="radio"][name="selectedtype"].minimal,input[type="radio"][name="selecteddate"].minimal');
    radioSchedule.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $('#arrange-transport-modal').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (arrangedTable.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }
        $(this).find('#schedule-date').addClass('hidden');
        $(this).find('#selectedDate').addClass('hidden')
        //check is already in transit
        var rowdata = arrangedTable.rows({
            selected: true,
            page: 'current'
        }).data();

        for (var i = 0; i < rowdata.length; i++) {
            if (rowdata[i].transportationStatus == 1) {
                if (rowdata.length == 1) {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }

        var element;
        var i = 0;
        arrangedTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = arrangedTable.row(this).data();

            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var remarks = ifNotValid(isExistNested(data, 'transportInfo', 'remarks') ? data.transportInfo.remarks : '', '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(isExistNested(data, 'purchaseInfo', 'auctionInfo', 'lotNo') ? data.purchaseInfo.auctionInfo.lotNo : '', '');
            var posNo = ifNotValid(isExistNested(data, 'purchaseInfo', 'auctionInfo', 'posNo') ? data.purchaseInfo.auctionInfo.posNo : '', '');
            var numberPlate = ifNotValid(data.numberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort, '');
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var charge = ifNotValid(data.charge, '');
            var pickupLocation = '';
            var pickupLocationCustom = '';
            var dropLocation = '';
            var dropLocationCustom = '';
            if (!isEmpty(data.transportationStatus) && data.transportationStatus == 0 && isEmpty(data.lastTransportLocation)) {
                pickupLocation = ifNotValid(isExistNested(data, 'transportInfo', 'pickupLocation') ? data.transportInfo.pickupLocation : '', '');
                pickupLocationCustom = ifNotValid(isExistNested(data, 'transportInfo', 'pickupLocationCustom') ? data.transportInfo.pickupLocationCustom : '', '');
                dropLocation = ifNotValid(isExistNested(data, 'transportInfo', 'dropLocation') ? data.transportInfo.dropLocation : '', '');
                dropLocationCustom = ifNotValid(isExistNested(data, 'transportInfo', 'dropLocationCustom') ? data.transportInfo.dropLocationCustom : '', '');
            } else {
                pickupLocation = ifNotValid(data.lastTransportLocation, '');
                pickupLocationCustom = ifNotValid(data.lastTransportLocationCustom, '');
                dropLocation = '';
                dropLocationCustom = '';
            }

            var transporter = ifNotValid(isExistNested(data, 'transportInfo', 'transporter') ? data.transportInfo.transporter : '', '');
            element = $('#item-vechicle-clone').find('.item-vehicle').clone();
            if (i != 0) {

                $(element).appendTo('#item-vehicle-clone-container');
                $('.charge').autoNumeric('init');
            } else {
                element = $('#item-vehicle-container').find('.item-vehicle');
            }

            $(element).find('.select2-select').select2({
                allowClear: true,
                width: '100%'
            });
            $(element).find('.datepicker').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            }).on('change', function() {
                $(this).valid();

            })
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('textarea[name="remarks"]').val(remarks);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('input[name="posNo"]').val(posNo);
            $(element).find('input[name="numberPlate"]').val(numberPlate);
            $(element).find('select[name="destinationCountry"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(countryJson, function(item) {
                    return {
                        id: item.country,
                        text: item.country,
                        data: item
                    };
                })
            }).val(destinationCountry).trigger('change');
            $(element).find('select[name="destinationPort"]').val(destinationPort).trigger('change');
            $(element).find('input[name="data"]').val(transporter);
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);

            var closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
            if (pickupLocation.toLowerCase() === 'others') {
                closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(pickupLocationCustom)
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            closest_container = $(element).find('select[name="dropLocation"]').closest('.form-group');
            if (dropLocation.toLowerCase() === 'others') {
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(dropLocationCustom)
                $(element).find('select[name="dropLocation"]').val(dropLocation).trigger("change");
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="dropLocation"]').val(dropLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            $(element).find('input[name="charge"]').autoNumeric('init').autoNumeric('set', ifNotValid(charge, 0));
            i++;
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-vehicle-container').find('#item-vehicle-clone-container').html('');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[type="text"]').val('');
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#transport-remark').find('textarea').val('');
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2').val('').trigger('change');
    }).on('change', '.select2-select.with-others', function() {
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.others-input-container').removeClass('hidden');
            $(closest_container).find('span.select2').addClass('hidden');
        }
    }).on('ifChecked', 'input[name="selectedtype"]', function() {
        if ($(this).val() == 0) {
            $('#schedule-date').addClass('hidden');
        } else if ($(this).val() == 1) {
            $('#schedule-date').addClass('hidden');
        } else if ($(this).val() == 2) {
            $('#schedule-date').removeClass('hidden');
        }
    }).on('click', 'a.show-dropdown', function() {
        var closest_container = $(this).closest('.form-group');
        $(closest_container).find('select.select2-select').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('span.select2').removeClass('hidden');
        $(closest_container).find('div.others-input-container').addClass('hidden');

    }).on('change', 'select[name="pickupLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');

    }).on('change', 'select[name="dropLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');
    }).on('change', 'select[name="transporter"]', function() {
        var charge = $(this).closest('.item-vehicle').find('input[name="charge"]');
        var valData = $(this).find('option:selected').data('data');
        if (!isEmpty(valData)) {
            if (!isEmpty(valData.data)) {
                $(charge).autoNumeric('init').autoNumeric('set', ifNotValid(valData.data.amount, 0));
            }
        } else {
            $(charge).autoNumeric('init').autoNumeric('set', 0);

        }
    }).on('change', 'select[name="destinationCountry"]', function() {
        var data = $(this).find(':selected').data('data');
        var destinationPort = $(this).closest('.item-vehicle').find('select[name="destinationPort"]');
        destinationPort.empty();
        data = data.data;
        if (!isEmpty(data)) {
            destinationPort.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item

                    };
                })
            }).val('').trigger("change");
        }
    });

    //Save Arrange Transport
    $('#btn-create-transport-order').on('click', function() {
        if (!$('#transport-arrangement-form').valid()) {
            return;
        }
        var objectArr = [];
        var data = {};
        autoNumericSetRawValue($('#arrange-transport-modal').find('.charge'))
        var scheduleDetails = getFormData($('#transport-schedule-details').find('.schedule-data'));
        var transportComment = getFormData($('#transport-comment').find('.comment'));
        $("#item-vehicle-container").find('.item-vehicle').each(function() {
            var object = {};
            object = getFormData($(this).find('input,select,textarea'));
            object.pickupDate = scheduleDetails.pickupDate;
            object.pickupTime = scheduleDetails.pickupTime;
            object.deliveryDate = scheduleDetails.deliveryDate;
            object.scheduleType = scheduleDetails.selectedtype;
            object.deliveryTime = scheduleDetails.deliveryTime;
            object.selectedDate = scheduleDetails.selecteddate;
            object.comment = transportComment.comment;
            objectArr.push(object);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/transport/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    arrangedTable.ajax.reload();
                    $('#arrange-transport-modal').modal('toggle');
                    //                     var alertEle = $('#alert-block');
                    //                     $(alertEle).css('display', '').html('<strong>Success!</strong> Transport order created.').fadeTo(2000, 500);
                }

            }
        });
    })

    //stock details modal update
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: ''
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

    //excel export form
    $(document).on('click', 'button#inspectionAvailablExport', function(event) {
        event.preventDefault();
        var form = $('<form>', {
            'action': myContextPath + '/inspection/stock/excel/report',
            'target': '_top',
            'method': 'post'
        })
        let items = table.rows({
            selected: true,
            page: 'all'
        }).data().toArray();
        for (let i = 0; i < items.length; i++) {
            form.append($('<input>', {
                'name': 'stockNos[]',
                'value': items[i].stockNo,
                'type': 'hidden'
            }));
        }

        $('body').append(form);
        form.submit();
        form.remove();
    });

})
function getTransporter(pickupLocEle, dropLocEle) {
    var pickupLoc = pickupLocEle.val();
    var dropLoc = dropLocEle.val();
    var autopopulate = false;
    var result = [];
    if (pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() == 'OTHERS') {
        result = transportersJson;
    } else if ((pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') || (dropLoc.toUpperCase() == 'OTHERS' && pickupLoc.toUpperCase() != 'OTHERS')) {
        result = transportersJson;
    } else if (pickupLoc.toUpperCase() != 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') {
        var response = getTransporterFee(pickupLocEle);
        result = response.data.list;
        autopopulate = response.data.autopopulate;
        //        if (isEmpty(response.data) || response.data.length == 0) {
        //            result = transportersJson;
        //        } else {
        //            result = response.data;
        //            autopopulate = true;
        //        }
    }
    var data = {};
    data["autopopulate"] = autopopulate;
    data["transportArr"] = result;
    return data;
}
function getTransporterFee(element) {
    var response;
    var closest_container = element.closest('.item-vehicle');
    var category = closest_container.find('input[name="subcategory"]').val();
    var from = closest_container.find('select[name="pickupLocation"]').val()
    var to = closest_container.find('select[name="dropLocation"]').val()
    var queryString = "?category=" + category + "&from=" + from + "&to=" + to
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/transport/charge" + queryString,
        success: function(data) {
            response = data;
        }
    })
    return response;
}

function isPickupAndDropIsSame(element) {
    var container = $(element).closest('.item-vehicle')
    var from = container.find('select[name="pickupLocation"]').val();
    var to = container.find('select[name="dropLocation"]').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}
function setDatatableColumns(datatable, columnsshow) {
    var index = datatable.columns().indexes();
    var columnshide = $.grep(index, function(el) {
        return $.inArray(el, columnsshow) == -1
    });
    datatable.columns(columnsshow).visible(true);
    datatable.columns(columnshide).visible(false);
}
function showArrangedStock() {
    $('#avail-vehicle-container').addClass('hidden');
    $('#arrange-transport-btn').removeClass('hidden');
    $('#inspection-btn').addClass('hidden');
    $('#arrange-vehicle-container').removeClass('hidden');
    $('.available-vehicles-filter').addClass('hidden');
    $('.inspection-arranged-filter').removeClass('hidden');
    $('#document-data-container').removeClass('hidden');
    $('#avail-instruction-vehicle-container').addClass('hidden');
    $('#arrange-instruction').addClass('hidden');
    $('#instruction-destination-filter').addClass('hidden');
    $('#inspection-cancelled').addClass('hidden');
    $('#instruction-completed-vehicle-container').addClass('hidden');
    $('#passed-vehicles').removeClass('hidden');
    $('#inspection-completed-div').addClass('hidden');
    $('#inspection-completed-div').addClass('hidden');
    $('#transport-not-complete').removeClass('hidden');

}

function showCompletedStock() {
    $('#avail-vehicle-container').addClass('hidden');
    $('#arrange-transport-btn').removeClass('hidden');
    $('#inspection-btn').addClass('hidden');
    $('#instruction-completed-vehicle-container').removeClass('hidden');
    $('#arrange-vehicle-container').addClass('hidden');
    $('.available-vehicles-filter').addClass('hidden');
    $('.inspection-arranged-filter').addClass('hidden');
    $('#document-data-container').removeClass('hidden');
    $('#avail-instruction-vehicle-container').addClass('hidden');
    $('#arrange-instruction').addClass('hidden');
    $('#instruction-destination-filter').addClass('hidden');
    $('#inspection-cancelled').addClass('hidden');
    $('#passed-vehicles').addClass('hidden');
    $('#inspection-completed-div').removeClass('hidden');
    $('#transport-not-complete').addClass('hidden');

}
function showAvailableStock() {
    $('#avail-vehicle-container').removeClass('hidden');
    $('#arrange-vehicle-container').addClass('hidden');
    $('#inspection-btn').addClass('hidden');
    $('#arrange-transport-btn').addClass('hidden');
    $('.available-vehicles-filter').removeClass('hidden');
    $('.inspection-arranged-filter').addClass('hidden');
    $('#document-data-container').addClass('hidden');
    $('#avail-instruction-vehicle-container').addClass('hidden');
    $('#arrange-instruction').removeClass('hidden');
    $('#instruction-destination-filter').addClass('hidden');
    $('#inspection-cancelled').addClass('hidden');
    $('#instruction-completed-vehicle-container').addClass('hidden');
    $('#passed-vehicles').addClass('hidden');
    $('#inspection-completed-div').addClass('hidden');
    $('#transport-not-complete').addClass('hidden');
}
function arraShippingInstrctionOrInspection() {
    $('#arrange-instruction').addClass('hidden');
    $('#inspection-btn').removeClass('hidden');
    $('#avail-vehicle-container').addClass('hidden');
    $('#arrange-vehicle-container').addClass('hidden');
    $('#avail-instruction-vehicle-container').removeClass('hidden');
    $('.inspection-arranged-filter').addClass('hidden')
    $('.available-vehicles-filter').addClass('hidden');
    $('#instruction-destination-filter').removeClass('hidden');
    $('#inspection-cancelled').addClass('hidden');
    $('#instruction-completed-vehicle-container').addClass('hidden');
    $('#passed-vehicles').addClass('hidden');
    $('#inspection-completed-div').addClass('hidden');
    $('#transport-not-complete').addClass('hidden');
}
function showCancelledOrders() {
    $('#arrange-instruction').addClass('hidden');
    $('#inspection-btn').addClass('hidden');
    $('#avail-vehicle-container').addClass('hidden');
    $('#arrange-vehicle-container').addClass('hidden');
    $('#avail-instruction-vehicle-container').addClass('hidden');
    $('.inspection-arranged-filter').addClass('hidden')
    $('.available-vehicles-filter').addClass('hidden');
    $('#instruction-destination-filter').addClass('hidden');
    $('#inspection-cancelled').removeClass('hidden');
    $('#instruction-completed-vehicle-container').addClass('hidden');
    $('#passed-vehicles').addClass('hidden');
    $('#inspection-completed-div').addClass('hidden');
    $('#transport-not-complete').addClass('hidden');
}

function format(rowData) {
    var element = $('#clone-container>#inspection-order-item>.clone-element').clone();

    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    $(element).find('table>thead th.arrival-date').addClass('hidden');
    var flag = $('input[name="radioShowTable"]:checked').val();
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();

        var status;
        var className;
        var transportationStatus;

        if (rowData.items[i].transportationStatus == 0) {
            transportationStatus = "Initiated"
            className = "default"

        } else if (rowData.items[i].transportationStatus == 1) {
            transportationStatus = "In Transit"
            className = "primary"
        } else if (rowData.items[i].transportationStatus == 2) {
            transportationStatus = "Completed"
            className = "success"
        }

        var transportStatus = '<span class="label label-' + className + '" style="width: 200px">' + transportationStatus + '</span>';

        if (ifNotValid(rowData.items[i].doumentSentStatus, 0) == 0) {
            status = "Not Sent"
            className = "default"

        } else if (ifNotValid(rowData.items[i].doumentSentStatus, 0) == 1) {
            status = "Copy"
            className = "primary"

        } else if (ifNotValid(rowData.items[i].doumentSentStatus, 0) == 2) {
            status = "Original"
            className = "success"

        }

        var docStatus = '<span class="label label-' + className + '" style="width: 200px">' + status + '</span>';

        var photoStatus;
        var photoClassName;
        if (ifNotValid(rowData.items[i].isPhotoUploaded, 0) == 0) {
            status = "No Photo"
            className = "default"

        } else if (ifNotValid(rowData.items[i].isPhotoUploaded, 0) == 1) {
            status = "Photo OK"
            className = "success"

        }
        var photoUploaded = '<span class="label label-' + className + '" style="width: 200px">' + status + '</span>';

        var text = '';
        if (rowData.items[i].shippingInstructionStatus == 0) {
            text = 'Immediate';
        } else if (rowData.items[i].shippingInstructionStatus == 1) {
            text = 'Next Available';
        } else if (rowData.items[i].shippingInstructionStatus == 2) {
            text = rowData.items[i].estimatedDeparture;
        }
        var actionhtml = ''
        if (rowData.items[i].status == 0) {
            actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" data-flag= "0" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
            if (rowData.items[i].transportationStatus != 2) {
                actionhtml += '<button class="ml-5 btn btn-success btn-xs" data-flag= "0" title="Pass" name="inspection-passed" disabled><i class="fa fa-fw fa-check"></i></button>'
            } else {
                actionhtml += '<button class="ml-5 btn btn-success btn-xs" data-flag= "0" title="Pass" name="inspection-passed"><i class="fa fa-fw fa-check"></i></button>'
            }
            actionhtml += '<a href="#" class="ml-5 btn btn-warning btn-xs"  data-flag= "0"data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false" title="Failed"><i class="fa fa-fw fa-close"></i></a>'
            actionhtml += '<a href="#" class="ml-5 btn btn-danger btn-xs" data-flag= "0" title="Delete" name="inspection-delete"><i class="fa fa-fw fa-trash-o"></i></a>'
        } else if (rowData.items[i].status == 5) {
            actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
            actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
        } else if (rowData.items[i].status == 3) {
            actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
        }

        var inspectionCompany = ifNotValid(rowData.items[i].inspectionCompanyFlag, '') == 0 ? ifNotValid(rowData.items[i].inspectionCompany, '') : ifNotValid(rowData.items[i].forwarder, '');
        row.attr('data-json', JSON.stringify(rowData.items[i]))
        row.attr('data-id', rowData.items[i].inspectionId)
        //         $(row).find('td.sno').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.model').html(ifNotValid(rowData.items[i].model, ''));
        $(row).find('td.purchasedDate').html(ifNotValid(rowData.items[i].purchaseInfo.sDate, ''));
        $(row).find('td.year').html(ifNotValid(rowData.items[i].firstRegDate, ''));
        $(row).find('td.color').html(ifNotValid(rowData.items[i].color, ''));
        $(row).find('td.finalPort').html(ifNotValid(rowData.items[i].sLastTransportLocation, ''));
        $(row).find('td.supplier').html(ifNotValid(rowData.items[i].supplierName, ''));
        $(row).find('td.shuppin').html(ifNotValid(rowData.items[i].purchaseInfo.auctionInfo.lotNo, ''));
        $(row).find('td.photo').html(photoUploaded);
        $(row).find('td.book').html(ifNotValid(rowData.items[i].bookingDetails, ''));
        $(row).find('td.estimatedDeparture').html(text);
        $(row).find('td.transporter').html(ifNotValid(rowData.items[i].transporterName, ''));
        $(row).find('td.frwdr-inspection-company').html(inspectionCompany);
        $(row).find('td.sentDate').html(ifNotValid(rowData.items[i].inspectionDate, ''));
        $(row).find('td.mashoCopy').html(ifNotValid(rowData.items[i].mashoCopyReceivedDate, ''));
        $(row).find('td.dateOfIssue').html(ifNotValid(rowData.items[i].inspectedDate, ''));
        $(row).find('td.docSentDate').html(ifNotValid(rowData.items[i].documentSentDate, ''));
        $(row).find('td.docStatus').html(docStatus);
        $(row).find('td.transportStatus').html(transportStatus);
        $(row).find('td.certificateNo').html(ifNotValid(rowData.items[i].certificateNo, ''));
        $(row).find('td.action').html(actionhtml);
        $(row).find('td.engineNo').html(ifNotValid(rowData.items[i].engineNo, ''));
        $(row).find('td.remark').html(ifNotValid(rowData.items[i].cancelRemark, ''));
        $(row).removeClass('hide');

        $(element).find('table>tbody').append(row);

    }
    if (flag == 2) {

        $(element).find('table>thead').find('tr th.dateOfIssue,th.certificateNo,th.engineNo,th.remark').addClass('hidden');
        $(element).find('table>tbody').find('tr td.dateOfIssue,td.certificateNo,td.engineNo,td.remark').addClass('hidden');
    } else if (flag == 3) {
        $(element).find('table>thead').find('tr th.remark').addClass('hidden');
        $(element).find('table>tbody').find('tr td.remark').addClass('hidden');
    } else if (flag == 4) {
        $(element).find('table>thead').find('tr th.action,th.dateOfIssue,th.certificateNo,th.engineNo').addClass('hidden');
        $(element).find('table>tbody').find('tr td.action,td.dateOfIssue,td.certificateNo,td.engineNo').addClass('hidden');
    }
    return element;
}

function format1(rowData) {
    var element = $('#clone-container>#inspection-order-item>.clone-element').clone();

    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    $(element).find('table>thead th.arrival-date').addClass('hidden');
    var flag = $('input[name="radioShowTable"]:checked').val();
    for (var i = 0; i < rowData.length; i++) {
        var row = $(rowClone).clone();

        var status;
        var className;
        var transportationStatus;

        if (rowData[i].transportationStatus == 0) {
            transportationStatus = "Initiated"
            className = "default"

        } else if (rowData[i].transportationStatus == 1) {
            transportationStatus = "In Transit"
            className = "primary"
        } else if (rowData[i].transportationStatus == 2) {
            transportationStatus = "Completed"
            className = "success"
        }

        var transportStatus = '<span class="label label-' + className + '" style="width: 200px">' + transportationStatus + '</span>';

        if (ifNotValid(rowData[i].doumentSentStatus, 0) == 0) {
            status = "Not Sent"
            className = "default"

        } else if (ifNotValid(rowData[i].doumentSentStatus, 0) == 1) {
            status = "Copy"
            className = "primary"

        } else if (ifNotValid(rowData[i].doumentSentStatus, 0) == 2) {
            status = "Original"
            className = "success"

        }

        var docStatus = '<span class="label label-' + className + '" style="width: 200px">' + status + '</span>';

        var photoStatus;
        var photoClassName;
        if (ifNotValid(rowData[i].isPhotoUploaded, 0) == 0) {
            status = "No Photo"
            className = "default"

        } else if (ifNotValid(rowData[i].isPhotoUploaded, 0) == 1) {
            status = "Photo OK"
            className = "success"

        }
        var photoUploaded = '<span class="label label-' + className + '" style="width: 200px">' + status + '</span>';

        var text = '';
        if (rowData[i].shippingInstructionStatus == 0) {
            text = 'Immediate';
        } else if (rowData[i].shippingInstructionStatus == 1) {
            text = 'Next Available';
        } else if (rowData[i].shippingInstructionStatus == 2) {
            text = rowData[i].estimatedDeparture;
        }
        var actionhtml = ''
        if (rowData[i].status == 0) {
            actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" data-flag= "0" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
            if (rowData[i].transportationStatus != 2) {
                actionhtml += '<button class="ml-5 btn btn-success btn-xs" data-flag= "0" title="Pass" name="inspection-passed" disabled><i class="fa fa-fw fa-check"></i></button>'
            } else {
                actionhtml += '<button class="ml-5 btn btn-success btn-xs" data-flag= "0" title="Pass" name="inspection-passed"><i class="fa fa-fw fa-check"></i></button>'
            }
            actionhtml += '<a href="#" class="ml-5 btn btn-warning btn-xs"  data-flag= "0"data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false" title="Failed"><i class="fa fa-fw fa-close"></i></a>'
            actionhtml += '<a href="#" class="ml-5 btn btn-danger btn-xs" data-flag= "0" title="Delete" name="inspection-delete"><i class="fa fa-fw fa-trash-o"></i></a>'
        } else if (rowData[i].status == 5) {
            actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
            actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
        } else if (rowData[i].status == 3) {
            actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "0" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
        }

        var inspectionCompany = ifNotValid(rowData[i].inspectionCompanyFlag, '') == 0 ? ifNotValid(rowData[i].inspectionCompany, '') : ifNotValid(rowData[i].forwarder, '');
        row.attr('data-json', JSON.stringify(rowData[i]))
        row.attr('data-id', rowData[i].inspectionId)
        //         $(row).find('td.sno').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData[i].chassisNo, ''));
        $(row).find('td.model').html(ifNotValid(rowData[i].model, ''));
        $(row).find('td.purchasedDate').html(ifNotValid(rowData[i].purchaseInfo.sDate, ''));
        $(row).find('td.year').html(ifNotValid(rowData[i].firstRegDate, ''));
        $(row).find('td.color').html(ifNotValid(rowData[i].color, ''));
        $(row).find('td.finalPort').html(ifNotValid(rowData[i].sLastTransportLocation, ''));
        $(row).find('td.supplier').html(ifNotValid(rowData[i].supplierName, ''));
        $(row).find('td.shuppin').html(ifNotValid(rowData[i].purchaseInfo.auctionInfo.lotNo, ''));
        $(row).find('td.photo').html(photoUploaded);
        $(row).find('td.book').html(ifNotValid(rowData[i].bookingDetails, ''));
        $(row).find('td.estimatedDeparture').html(text);
        $(row).find('td.transporter').html(ifNotValid(rowData[i].transporterName, ''));
        $(row).find('td.frwdr-inspection-company').html(inspectionCompany);
        $(row).find('td.sentDate').html(ifNotValid(rowData[i].inspectionDate, ''));
        $(row).find('td.mashoCopy').html(ifNotValid(rowData[i].mashoCopyReceivedDate, ''));
        $(row).find('td.dateOfIssue').html(ifNotValid(rowData[i].inspectedDate, ''));
        $(row).find('td.docSentDate').html(ifNotValid(rowData[i].documentSentDate, ''));
        $(row).find('td.docStatus').html(docStatus);
        $(row).find('td.transportStatus').html(transportStatus);
        $(row).find('td.certificateNo').html(ifNotValid(rowData[i].certificateNo, ''));
        $(row).find('td.action').html(actionhtml);
        $(row).find('td.engineNo').html(ifNotValid(rowData[i].engineNo, ''));
        $(row).find('td.remark').html(ifNotValid(rowData[i].cancelRemark, ''));
        $(row).removeClass('hide');

        $(element).find('table>tbody').append(row);

    }
    if (flag == 2) {

        $(element).find('table>thead').find('tr th.dateOfIssue,th.certificateNo,th.engineNo,th.remark').addClass('hidden');
        $(element).find('table>tbody').find('tr td.dateOfIssue,td.certificateNo,td.engineNo,td.remark').addClass('hidden');
    } else if (flag == 3) {
        $(element).find('table>thead').find('tr th.remark').addClass('hidden');
        $(element).find('table>tbody').find('tr td.remark').addClass('hidden');
    } else if (flag == 4) {
        $(element).find('table>thead').find('tr th.action,th.dateOfIssue,th.certificateNo,th.engineNo').addClass('hidden');
        $(element).find('table>tbody').find('tr td.action,td.dateOfIssue,td.certificateNo,td.engineNo').addClass('hidden');
    }
    return element;
}
