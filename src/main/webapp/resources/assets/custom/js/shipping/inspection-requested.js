var tableInstruction;
$(function() {

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        tableInstruction.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        tableInstruction.page.len($(this).val()).draw();
    });

    //Available Vehicle Table
    tableInstruction = $('#table-inspection-instruction').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 100,
        "ajax": {
            url: myContextPath + "/inspection/instruction-data",
            beforeSend: function() {
                $('div#avail-instruction-vehicle-container>div.overlay').show()
            },
            complete: function() {
                $('div#avail-instruction-vehicle-container>div.overlay').hide();
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
            "data": "firstRegDate"
        }, {
            targets: 4,
            "data": "color"
        }, {
            targets: 5,
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
            targets: 6,
            "data": "destinationCountry"
        }, {
            targets: 7,
            "data": "supplierName"
        }, {
            targets: 8,
            "data": "lotNo"
        }, {
            targets: 9,
            "data": "isPhotoUploaded",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Not Arranged';
                    var className = "label-default";
                    let isHidden = '';
                    if (data == 0) {
                        isHidden = 'hidden';
                        status = 'NO PHOTO';
                        className = "label-default";
                    } else if (data == 1) {
                        isHidden = '';
                        status = 'PHOTO OK';
                        className = "label-success";
                    }
                    let html = '';
                    html += '<span class="label ' + className + '" style="min-width:100px">' + status + '</span><br>'
                    html += '<a class="ml-5 btn btn btn-warning btn-xs ' + isHidden + '" title="Photo Not Received" name="photoNotReceived" id="photoNotReceived">change</a>'
                    return html
                }
                return data;
            }
        }, {
            targets: 10,
            "data": "documentReceivedDate"
        }, {
            targets: 11,
            "data": "etd",
            visible: false
        }, {
            targets: 12,
            "data": "bookingDetails"
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
                //                 if (row.chassisNo == "M900A-0034304") {
                //                     console.log(status + "" + className)
                //                 }
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
                    if (data === 0) {
                        status = 'Shipping Arranged';
                        className = "label-warning";
                    }
                    if (data === 1) {
                        status = 'Shipping Confirmed';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }

        }, {
            targets: 21,
            "render": function(data, type, row) {
                let html = '';
                html += '<a href="#" class="ml-5 btn btn-danger btn-xs cancel-inspection" title="Cancel"><i class="fa fa-fw fa-close"></i>Cancel</a>';
                return html;
            }

        }]

    });
    $(document).on('click', 'button#inspectionRequestedExport', function(event) {
        event.preventDefault();
        var form = $('<form>', {
            'action': myContextPath + '/inspection/requested/stock/excel/report',
            'target': '_top',
            'method': 'post'
        })
        let items = tableInstruction.rows({
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
    tableInstruction.on('click', 'a.cancel-inspection', function() {
        let tr = $(this).closest('tr');
        let data = tableInstruction.row(tr).data();
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
            url: myContextPath + "/inspection/request/cancel?id=" + data.code,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableInstruction.ajax.reload();
                }
            }
        })
    })
    tableInstruction.on('click', 'a[name="photoNotReceived"]', function(e) {
        let tr = $(this).closest('tr');
        let data = tableInstruction.row(tr).data();
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
            url: myContextPath + "/inspection/stock/update/photoNotReceived/single?stockNo=" + data.stockNo,
            contentType: "application/json",
            success: function(response) {
                if (response.status === 'success') {
                    let row = tableInstruction.row(tr)
                    data.isPhotoUploaded = 0;
                    row.data(data).invalidate();
                }
            }
        })
    })
    // status update on photo button
    $('#upload-photo').on('click', function(event) {
        if (tableInstruction.rows({
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
        tableInstruction.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let rowData = {};
            var data = tableInstruction.row(this).data();
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
                    tableInstruction.ajax.reload();
                }
            }
        })
    })

    // status update on photo button
    $('div#inspectionRequestedMashoCopyReceivedModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (tableInstruction.rows({
            selected: true,
            page: 'all'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }

        $('#inspectionRequestedMashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').datepicker('setDate', new Date())

    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
    }).on('click', 'button#save', function() {

        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        if (!$('div#inspectionRequestedMashoCopyReceivedModal form#form-mashoCopyReceivedModal').valid()) {
            return false;
        }
        let date = $('div#inspectionRequestedMashoCopyReceivedModal').find('input[name="mashiCopyReceivedDate"]').val();
        var data_stock = [];
        tableInstruction.rows({
            selected: true,
            page: 'all'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            let rowData = {};
            var data = tableInstruction.row(this).data();
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
                    tableInstruction.ajax.reload();
                    $('div#inspectionRequestedMashoCopyReceivedModal').modal('toggle');
                }
            }
        })
    })
    //     $('#mashoCopyReceived').on('click', function(event) {
    //         if (tableInstruction.rows({
    //             selected: true,
    //             page: 'current'
    //         }).count() == 0) {
    //             alert($.i18n.prop('common.alert.stock.noselection'));
    //             return event.preventDefault();
    //         }

    //         if (!confirm($.i18n.prop('common.confirm.update'))) {
    //             return false;
    //         }

    //         var data_stock = [];
    //         tableInstruction.rows({
    //             selected: true,
    //             page: 'current'
    //         }).every(function(rowIdx, tableLoop, rowLoop) {
    //             let rowData = {};
    //             var data = tableInstruction.row(this).data();
    //             rowData['stockNo'] = data.stockNo;
    //             rowData['chassisNo'] = data.chassisNo;
    //             data_stock.push(rowData);
    //         })

    //         $.ajax({
    //             beforeSend: function() {
    //                 $('#spinner').show()
    //             },
    //             complete: function() {
    //                 $('#spinner').hide();
    //             },
    //             type: "put",
    //             data: JSON.stringify(data_stock),
    //             url: myContextPath + "/inspection/stock/update/mashoCopyReceived",
    //             contentType: "application/json",
    //             success: function(data) {
    //                 if (data.status === 'success') {
    //                     tableInstruction.ajax.reload();
    //                 }
    //             }
    //         })
    //     })

    //Arrange Inspection Modal and available vehicle save
    var inspectionModalEle = $('#arrange-inspection-modal');

    inspectionModalEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (tableInstruction.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }

        if (isEmpty($('#filter-destination-country-instruction').val())) {
            alert($.i18n.prop('alert.instruction.select.destCountry'));
            return event.preventDefault();
        }
        let inspectionModalTriggerEle = $(event.relatedTarget);
        inspectionModalEle.find('select[name="country"]').val($('#filter-destination-country-instruction').val()).trigger('change');
    }).on('hidden.bs.modal', function() {
        $(this).find("input:not(input[type='radio']),textarea,select").val('').end();
        $(this).find('select').val('').trigger('change');
    }).on('ifChecked', 'input[name="inspectionCompanyFlag"]', function() {
        if ($(this).val() == 0) {
            $('#arrange-inspection-modal').find('.inspection-company').removeClass('hidden');
            $('#arrange-inspection-modal').find('.forwarder-company').addClass('hidden').find('select').val('').trigger('change');
            ;
        } else if ($(this).val() == 1) {
            $('#arrange-inspection-modal').find('.inspection-company').addClass('hidden').find('select').val('').trigger('change');
            $('#arrange-inspection-modal').find('.forwarder-company').removeClass('hidden')
        }
    }).on('click', '#inspection-submit', function() {
        var data_stock = [];
        if (!$('#form-arrange-inspection').valid()) {
            return;
        }

        tableInstruction.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableInstruction.row(this).data();
            let dataObject = {};
            dataObject['stockNo'] = data.stockNo;
            dataObject['instructionId'] = data.code;
            data_stock.push(dataObject);
        })
        var arrangeInspectionData = $('#arrange-inspection-modal');
        var object = getFormData(arrangeInspectionData.find('input,select,textarea'));
        var inspectionLocation = $('#arrange-inspection-modal select[name="inspectionLocation"] :selected').data('data');
        inspectionLocation = isEmpty(inspectionLocation) ? {} : inspectionLocation.data;
        var inspectionOrderData = [];
        for (var i = 0; i < data_stock.length; i++) {
            data = {};
            data.stockNo = data_stock[i].stockNo;
            data.instructionId = data_stock[i].instructionId;
            data.country = object.country;
            data.forwarder = isEmpty(object.forwarder) ? null : object.forwarder;
            data.comment = object.comment;
            data.inspectionCompanyFlag = object.inspectionCompanyFlag;
            data.inspectionCompany = object.inspectionCompany;
            data.inspectionDate = object.inspectionDate;
            data.location = inspectionLocation;
            inspectionOrderData.push(data);
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(inspectionOrderData),
            url: myContextPath + "/inspection/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableInstruction.ajax.reload()
                    arrangedTable.ajax.reload();
                    var alertEle = $('#alert-block');
                    //set status
                    setShippingDashboardStatus();
                    $('#arrange-inspection-modal').modal('toggle');
                }
            }
        })

    });
})
