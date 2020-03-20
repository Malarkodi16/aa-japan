var tableCancelled;
$(function() {

    $('#rearrange-inspection-modal').on('change', 'select[name="inspectionCompany"]', function() {
        var data = $(this).find(':selected').data('data');
        $('#rearrange-inspection-modal').find('select[name="inspectionLocation"]').empty();
        if (!isEmpty(data) && !isEmpty(data.data)) {
            $('#rearrange-inspection-modal').find('select[name="inspectionLocation"]').select2({
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

    $('input[type="radio"][name="radioInspectionDetail"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        let element = $(this);

        if ($(element).val() == 0) {

            $('#rearrange-inspection-modal').find('#selectedDate').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#destCountryhide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('input#aajValue').iCheck('check')
            // $('#rearrange-inspection-modal').find('#forwarderIdhide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#inspectionIdHide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#inspectionLocationIdHide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#inspDateHide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#commentHide').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#save-request-submit').addClass('hidden')
            $('#rearrange-inspection-modal').find('#reinspection-submit').removeClass('hidden')

            //table.ajax.reload()
        } else if ($(element).val() == 1) {
            $('#rearrange-inspection-modal').find('#selectedDate').addClass('hidden')
            $('#rearrange-inspection-modal').find('#destCountryhide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#forwarderIdhide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#inspectionIdHide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#inspectionLocationIdHide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#inspDateHide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#commentHide').addClass('hidden')
            $('#rearrange-inspection-modal').find('#save-request-submit').removeClass('hidden')
            $('#rearrange-inspection-modal').find('#reinspection-submit').addClass('hidden')

            //table_rearrange.ajax.reload()
        }
    })

    //Available Vehicle Table
    tableCancelled = $('#table-inspection-cancelled').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 100,
        "ajax": {
            url: myContextPath + "/inspection/cancelled/order",
            beforeSend: function() {
                $('div#inspection-cancelled>div.overlay').show()
            },
            complete: function() {
                $('div#inspection-cancelled>div.overlay').hide();
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "order": [[5, "desc"]],
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "model"
        }, {
            targets: 2,
            "data": "maker"
        }, {
            targets: 3,
            "data": "destinationCountry"
        }, {
            targets: 4,
            "data": "",
            "render": function(data, type, row) {
                if (!isEmpty(row.forwarder)) {
                    return row.forwarder
                } else {
                    return row.inspectionCompany
                }
            }
        }, {
            targets: 5,
            "data": "inspectionSentDate",
            "type": "date-eu"
        }, {
            targets: 6,
            "data": "lastTransportLocationId",
            "render": function(data, type, row) {
                if (isEmpty(data)) {
                    return "";
                } else if (!isEmpty(data) && data.toLowerCase() !== 'others') {
                    data = row.lastTransportLocationName;
                } else {
                    data = row.lastTransportLocationCustom;
                }
                return data;
            }
        }, {
            targets: 7,
            "data": "bookingDetails"
        }, {
            targets: 8,
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
            targets: 9,
            "data": "cancelRemark"
        }, {
            targets: 10,
            "data": "remark"
        }, {
            targets: 11,
            "render": function(data, type, row) {
                var html = '';
                html += '<button type="button" class="ml-5 btn-xs btn btn-default fa fa-pencil-square-o" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-cancelledRemark"></button>'
                html += '<button type="button" href="#" class="ml-5 btn-xs btn btn-primary fa fa-check" id="reArrange-inspection" title="Re-arrange Inspection" data-toggle="modal" data-target="#rearrange-inspection-modal" data-backdrop="static" data-keyboard="false"></button>'
                return html;
            }
        }]

    });

    //Arrange Inspection Modal and available vehicle save
    var reInspectionModalEle = $('#rearrange-inspection-modal');

    reInspectionModalEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        let reIinspectionModalTriggerEle = $(event.relatedTarget);
        var tr = $(reIinspectionModalTriggerEle).closest('tr');
        var rowData = tableCancelled.row(tr).data();
        reInspectionModalEle.find('input[name="code"]').val(rowData.code);
        reInspectionModalEle.find('select[name="country"]').val(rowData.destinationCountry).trigger('change');
        reInspectionModalEle.find('select[name="inspectionCompany"]').val(rowData.inspectionCompanyId).trigger('change');
        reInspectionModalEle.find('select[name="inspectionLocation"]').val(rowData.location.locationId).trigger('change');
        reInspectionModalEle.find('select[name="forwarder"]').val(rowData.forwarderId).trigger('change');
        reInspectionModalEle.find('input[name="inspectionDate"]').datepicker('setDate', rowData.inspectionSentDate);
        reInspectionModalEle.find('textarea[name="comment"]').val(rowData.comment);
        if (rowData.inspectionCompanyFlag == 0) {
            $('#rearrange-inspection-modal').find('input#aajValue').iCheck('check');
            $('#rearrange-inspection-modal').find('.inspection-company').removeClass('hidden');
            $('#rearrange-inspection-modal').find('.forwarder-company').addClass('hidden').find('select').val('').trigger('change');
        } else if (rowData.inspectionCompanyFlag == 1) {
            $('#rearrange-inspection-modal').find('input#forwarder').iCheck('check');
            $('#rearrange-inspection-modal').find('.inspection-company').addClass('hidden').find('select').val('').trigger('change');
            $('#rearrange-inspection-modal').find('.forwarder-company').removeClass('hidden')
        }
    }).on('hidden.bs.modal', function() {
        $(this).find("input:not(input[type='radio']),textarea,select").val('').end();
        $(this).find('select').val('').trigger('change');
        // resetElementInput(this)
    }).on('ifChecked', 'input[name="inspectionCompanyFlag"]', function() {
        if ($(this).val() == 0) {
            $('#rearrange-inspection-modal').find('.inspection-company').removeClass('hidden');
            $('#rearrange-inspection-modal').find('.forwarder-company').addClass('hidden').find('select').val('').trigger('change');

        } else if ($(this).val() == 1) {
            $('#rearrange-inspection-modal').find('.inspection-company').addClass('hidden').find('select').val('').trigger('change');
            $('#rearrange-inspection-modal').find('.forwarder-company').removeClass('hidden')
        }
    }).on('click', '#reinspection-submit', function() {
        if (!$('#form-rearrange-inspection').valid()) {
            return;
        }
        var rearrangeInspectionData = $('#rearrange-inspection-modal');
        var object = getFormData(rearrangeInspectionData.find('input,select,textarea'));
        var inspectionLocation = $('#rearrange-inspection-modal select[name="inspectionLocation"] :selected').data('data');
        var location = $('#rearrange-inspection-modal select[name="inspectionLocation"] :selected').text();
        var inspectionCompanyValue = $('#rearrange-inspection-modal select[name="inspectionCompany"] :selected').text();
        var forwarderValue = $('#rearrange-inspection-modal select[name="forwarder"] :selected').text();
        inspectionLocation = isEmpty(inspectionLocation) ? {} : inspectionLocation.data;

        data = {};
        data.country = object.country;
        data.forwarder = object.forwarder;
        data.comment = object.comment;
        data.inspectionCompanyFlag = object.inspectionCompanyFlag;
        data.inspectionCompany = object.inspectionCompany;
        data.inspectionDate = object.inspectionDate;
        data.location = inspectionLocation;
        data.locationValue = location;
        data.inspectionCompanyValue = inspectionCompanyValue;
        data.forwarderValue = forwarderValue;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/inspection/order/re-arrange?code=" + object.code,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableCancelled.ajax.reload()
                    $('#rearrange-inspection-modal').modal('toggle');
                }
            }
        })

    });

    $('#rearrange-inspection-modal').on('show.bs.modal', function(event) {
        var triggerElement = $(event.relatedTarget);
        var triggerrowindex = tableCancelled.row(triggerElement.closest('tr')).index();
        var tr = $(triggerElement).closest('tr');
        var rowData = tableCancelled.row(tr).data();
        $('#rearrange-inspection-modal').find("#code").val(rowData["code"]);
        $('#rearrange-inspection-modal').find("#stockNo").val(rowData["stockNo"]);
        $('#rearrange-inspection-modal').find("#instructionId").val(rowData["instructionId"]);
        $('#rearrange-inspection-modal').find("#destinationCountry").val(rowData["destinationCountry"]);
    }).on('hidden.bs.modal', function() {
        //         resetElementInput(this)
        $(this).find('input[name="radioInspectionDetail"][value="0"]').iCheck('check');
    }).on("click", '#save-request-submit', function(event) {
        var instructionId = $('#rearrange-inspection-modal').find('input[name="instructionId"]').val();
        var code = $('#rearrange-inspection-modal').find('input[name="code"]').val();
        var data = {};
        data.stockNo = $('#rearrange-inspection-modal').find('input[name="stockNo"]').val();
        data.destCountry = $('#rearrange-inspection-modal').find('input[name="destinationCountry"]').val();
        

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/inspection/instruction/moveToRequested?instructionId=" + instructionId + "&code=" + code,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload()
                    tableCompleted.ajax.reload()
                    tableInstruction.ajax.reload();
                    tableCancelled.ajax.reload();
                    var alertEle = $('#alert-block');
                    //set status
                    setShippingDashboardStatus();

                    $('#rearrange-inspection-modal').modal('toggle');
                }
            }
        })
    })

    //     tableCancelled.on('click', 'tr td #reArrange-inspection', function(event) {
    //         var isConfirmed = confirm($.i18n.prop('confirm.inspection.rearrangement'))
    //         if (isConfirmed) {
    //             tableCancelledTriggerEle = $(event.currentTarget);
    //             var rowData = tableCancelled.row((tableCancelledTriggerEle).closest('tr')).data();
    //             $.ajax({
    //                 beforeSend: function() {
    //                     $('#spinner').show()
    //                 },
    //                 complete: function() {
    //                     $('#spinner').hide();
    //                 },
    //                 type: "put",
    //                 url: myContextPath + "/inspection/order/re-arrange?code=" + rowData.code,
    //                 contentType: "application/json",
    //                 success: function(data) {
    //                     if (data.status === 'success') {
    //                         tableInstruction.ajax.reload()
    //                         arrangedTable.ajax.reload();
    //                         tableCancelled.ajax.reload();
    //                         var alertEle = $('#alert-block');
    //                        //set status
    //    setShippingDashboardStatus();
    //                     }
    //                 }
    //             })
    //         } else {
    //             console.log("return")
    //         }
    //     })
    $('#modal-cancelledRemark').on('show.bs.modal', function(event) {
        var triggerElement = $(event.relatedTarget);
        var tr = $(triggerElement).closest('tr');
        var data = tableCancelled.row(tr).data();

        $(this).find('input[name="data"]').val(JSON.stringify(data));

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-reason-submit', function(event) {
        var rowdata = $('#modal-cancelledRemark input[name="data"]').val();
        rowdata = JSON.parse(rowdata);
        var data = {};
        data["remark"] = $('#modal-cancelledRemark textarea[name="remark"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + '/inspection/request/cancelled/update/remark?id=' + rowdata.code,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableCancelled.ajax.reload();
                }
                $('#modal-cancelledRemark').modal('toggle');
            }
        });

    })
})
//date sort
jQuery.extend(jQuery.fn.dataTableExt.oSort, {
    "date-eu-pre": function(date) {
        date = date.replace(" ", "");

        if (!date) {
            return 0;
        }

        var year;
        var eu_date = date.split(/[\.\-\/]/);

        /*year (optional)*/
        if (eu_date[2]) {
            year = eu_date[2];
        } else {
            year = 0;
        }

        /*month*/
        var month = eu_date[1];
        if (month.length == 1) {
            month = 0 + month;
        }

        /*day*/
        var day = eu_date[0];
        if (day.length == 1) {
            day = 0 + day;
        }

        return (year + month + day) * 1;
    },

    "date-eu-asc": function(a, b) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },

    "date-eu-desc": function(a, b) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});
