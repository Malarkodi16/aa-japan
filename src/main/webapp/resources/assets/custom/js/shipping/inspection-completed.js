var tableCompleted;
var inspectionHistoryDetails = $('#inspectionHistoryDetails');
var inspectionHistoryDetailsTable;
var tableElement = inspectionHistoryDetails.find('table')
$(function() {

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        tableCompleted.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        tableCompleted.page.len($(this).val()).draw();
    });

   

    //Available Vehicle Table
    tableCompleted = $('#table-completed').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 100,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/inspection/inspection-data-completed",
            beforeSend: function() {
                $('div#instruction-completed-vehicle-container>div.overlay').show()
            },
            complete: function() {
                $('div#instruction-completed-vehicle-container>div.overlay').hide();
            }
        },
        select: {
            style: 'multiple',
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
            "render": function(data, type, row) {
                return row.purchaseInfo.sDate;
            }
        }, {
            targets: 4,
            "data": "firstRegDate"
        }, {
            targets: 5,
            "data": "color"
        }, {
            targets: 6,
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
            targets: 7,
            "data": "destinationCountry"
        }, {
            targets: 8,
            "data": "destinationPort"
        }, {
            targets: 9,
            "data": "country"
        }, {
            targets: 10,
            "data": "supplierName"
        }, {
            targets: 11,
            "render": function(data, type, row) {
                return row.purchaseInfo.auctionInfo.lotNo;
            }
        }, {
            targets: 12,
            "data": "isPhotoUploaded",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Not Arranged';
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
                return data;
            }
        }, {
            targets: 13,
            "data": "bookingDetails"
        }, {
            targets: 14,
            "data": "estimatedDeparture",
            "render": function(data, type, row) {
                var text = '';
                if (row.shippingInstructionStatus == 0) {
                    text = 'Immediate';
                } else if (row.shippingInstructionStatus == 1) {
                    text = 'Next Available';
                } else if (row.shippingInstructionStatus == 2) {
                    text = row.estimatedDeparture;
                }
                return text;
            }
        }, {
            targets: 15,
            "data": "transporterName"

        }, {
            targets: 16,
            "data": "inspectionCompanyFlag",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data == 0) {
                    return row.inspectionCompany
                } else {
                    return row.forwarder;
                }
            }
        }, {
            targets: 17,
            "data": "inspectionDate"
        }, {
            targets: 18,
            "data": "dateOfIssue"
        }, {
            targets: 19,
            "data": "documentSentDate"
        }, {
            targets: 20,
            "data": "doumentSentStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status;
                    var className;
                    if (data == 0) {
                        status = 'Not Sent';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'Copy';
                        className = "label-primary";
                    } else if (data == 2) {
                        status = 'Original';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }

        }, {
            targets: 21,
            "data": "certificateNo",
        }, {
            targets: 22,
            "data": "engineNo"
        }, {
            targets: 23,
            "data": "status",
            "width": "50px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var actionhtml = ''
                if (data == 0) {
                    actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-flag= "1" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
                    actionhtml += '<a href="#" class="ml-5 btn btn-success btn-xs" data-flag= "1" title="Pass" name="inspection-passed"><i class="fa fa-fw fa-check"></i></a>'
                    actionhtml += '<a href="#" class="ml-5 btn btn-warning btn-xs"  data-flag= "1" data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false" title="Failed"><i class="fa fa-fw fa-close"></i></a>'
                    actionhtml += '<a href="#" class="ml-5 btn btn-danger btn-xs" data-flag= "1" title="Delete" name="inspection-delete"><i class="fa fa-fw fa-trash-o"></i></a>'
                } else if (data == 5) {
                    actionhtml += '<a href="#" class="btn btn-info btn btn-default btn-xs" data-flag= "1" data-toggle="modal" data-target="#modal-inspection-document" data-backdrop="static" data-keyboard="false" title="Document Status Update"><i class="fa fa-fw fa-file-text-o"></i></a>';
                    actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "1" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
                } else if (data == 3) {
                    actionhtml += '<a href="#" class="btn btn-primary ml-5 btn btn-default btn-xs" data-flag= "1" data-toggle="modal" data-target="#modal-inspection" data-backdrop="static" data-keyboard="false" title="Completed"><i class="fa fa-fw fa-edit "></i></a>';
                    actionhtml += '<button type="button" class="btn btn-success ml-5 btn-xs" data-target="#modal-failed-history" title="Failed History" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                }

                return actionhtml;
            }
        }]

    });

    //invoice payments Detail Modal
    var inspectionFailedEle = $('#modal-failed-history')
    var inspectionFailedHistoryModalBody = inspectionFailedEle.find('#inspectionHistoryDetails');
    inspectionFailedEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData = tableCompleted.row($(event.relatedTarget).closest('tr')).data();
        inspectionFailedHistoryModalBody.find('input[name="inspectionCode"]').val(rowData.inspectionCode)
        setFailedHistoryData(rowData.stockNo);
        inspectionFailedHistoryModalBody.find('input[name="rowData"]').val(JSON.stringify(rowData));
        // inspectionFailedEle.find('span.invoiceNo').html(rowData.inspectionCode)
        // inspectionFailedEle.find('span.supplierName').html(rowData.supplierName + '/' + rowData.auctionHouseName)
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#inspectionHistoryDetails').find('table').dataTable().fnDestroy();
    });

    // status update on photo button
    //     $('#upload-photo').on('click', function(event) {
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
    //             url: myContextPath + "/inspection/stock/update/photoReceived",
    //             contentType: "application/json",
    //             success: function(data) {
    //                 if (data.status === 'success') {
    //                     tableInstruction.ajax.reload();
    //                 }
    //             }
    //         })
    //     })

    //     // status update on photo button
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

    //     //Arrange Inspection Modal and available vehicle save
    //     var inspectionModalEle = $('#arrange-inspection-modal');

    //     inspectionModalEle.on('show.bs.modal', function(event) {
    //         if (event.namespace != 'bs.modal') {
    //             return;
    //         }
    //         if (tableInstruction.rows({
    //             selected: true,
    //             page: 'current'
    //         }).count() == 0) {
    //             alert($.i18n.prop('common.alert.stock.noselection'));
    //             return event.preventDefault();
    //         }

    //         if (isEmpty($('#filter-destination-country-instruction').val())) {
    //             alert($.i18n.prop('alert.instruction.select.destCountry'));
    //             return event.preventDefault();
    //         }
    //         let inspectionModalTriggerEle = $(event.relatedTarget);
    //         inspectionModalEle.find('select[name="country"]').val($('#filter-destination-country-instruction').val()).trigger('change');
    //     }).on('hidden.bs.modal', function() {
    //         $(this).find("input:not(input[type='radio']),textarea,select").val('').end();
    //         $(this).find('select').val('').trigger('change');
    //     }).on('ifChecked', 'input[name="inspectionCompanyFlag"]', function() {
    //         if ($(this).val() == 0) {
    //             $('#arrange-inspection-modal').find('.inspection-company').removeClass('hidden');
    //             $('#arrange-inspection-modal').find('.forwarder-company').addClass('hidden').find('select').val('').trigger('change');
    //             ;
    //         } else if ($(this).val() == 1) {
    //             $('#arrange-inspection-modal').find('.inspection-company').addClass('hidden').find('select').val('').trigger('change');
    //             $('#arrange-inspection-modal').find('.forwarder-company').removeClass('hidden')
    //         }
    //     }).on('click', '#inspection-submit', function() {
    //         var data_stock = [];
    //         if (!$('#form-arrange-inspection').valid()) {
    //             return;
    //         }

    //         tableInstruction.rows({
    //             selected: true,
    //             page: 'current'
    //         }).every(function(rowIdx, tableLoop, rowLoop) {
    //             var data = tableInstruction.row(this).data();
    //             let dataObject = {};
    //             dataObject['stockNo'] = data.stockNo;
    //             dataObject['instructionId'] = data.code;
    //             data_stock.push(dataObject);
    //         })
    //         var arrangeInspectionData = $('#arrange-inspection-modal');
    //         var object = getFormData(arrangeInspectionData.find('input,select,textarea'));
    //         var inspectionLocation = $('#arrange-inspection-modal select[name="inspectionLocation"] :selected').data('data');
    //         inspectionLocation = isEmpty(inspectionLocation) ? {} : inspectionLocation.data;
    //         var inspectionOrderData = [];
    //         for (var i = 0; i < data_stock.length; i++) {
    //             data = {};
    //             data.stockNo = data_stock[i].stockNo;
    //             data.instructionId = data_stock[i].instructionId;
    //             data.country = object.country;
    //             data.forwarder = object.forwarder;
    //             data.comment = object.comment;
    //             data.inspectionCompanyFlag = object.inspectionCompanyFlag;
    //             data.inspectionCompany = object.inspectionCompany;
    //             data.inspectionDate = object.inspectionDate;
    //             data.location = inspectionLocation;
    //             inspectionOrderData.push(data);
    //         }
    //         $.ajax({
    //             beforeSend: function() {
    //                 $('#spinner').show()
    //             },
    //             complete: function() {
    //                 $('#spinner').hide();
    //             },
    //             type: "post",
    //             data: JSON.stringify(inspectionOrderData),
    //             url: myContextPath + "/inspection/save",
    //             contentType: "application/json",
    //             success: function(data) {
    //                 if (data.status === 'success') {
    //                     tableInstruction.ajax.reload()
    //                     arrangedTable.ajax.reload();
    //                     var alertEle = $('#alert-block');
    //                    //set status
    //    setShippingDashboardStatus();

    //                     $('#arrange-inspection-modal').modal('toggle');
    //                 }
    //             }
    //         })

    //     });
})

function setFailedHistoryData(code) {

    inspectionHistoryDetailsTable = tableElement.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": myContextPath + "/inspection/failed/history/data-source?stockNo=" + code,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "data": "inspectionCreatedDate",

        }, {
            targets: 1,
            "data": "inspectionDate",

        }, {
            targets: 2,
            "data": "inspectionDestCounrtry"
        }, {
            targets: 3,
            "data": "inspectionCountry",

        }, {
            targets: 4,
            "data": "inspectionCompany",

        }, {
            targets: 5,
            "data": "location",

        }, {
            targets: 6,
            "data": "inspectionFlag",

        }, {
            targets: 7,
            "data": "remarks",

        }]

    })

}
