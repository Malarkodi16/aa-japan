//multi step form init step
let create_lc_step = 0;
let modal_create_lc = $('#modal-create-lc');
$(function() {
    // validate validation enabled fields
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    // foreign bank data
    $.getJSON(myContextPath + "/data/foreignBanks.json", function(data) {
        $('#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.bankId,
                    text: item.bank,
                    data: item
                };
            })
        }).val('').trigger("change");
    });

    // Shipping Terms Filter
    $.getJSON(myContextPath + '/data/shippingTerms.json', function(data) {
        shippingTermsJson = data;
        $('#shippingTermsName').select2({
            allowClear: true,
            width: '100%',
            data: $.map(shippingTermsJson, function(item) {
                return {
                    id: item.termsId,
                    text: item.name,
                    data: item
                };
            })
        })
    })

    $('#shippingTermsName').on('change', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data.data)) {
            $('#shippingTerms').val(data.data.shippingTerms).trigger('change');

        } else if (isEmpty(data.data)) {
            $('#shippingTerms').val('').trigger('change');
        }
    })

    // Shipping Marks Filter
    $.getJSON(myContextPath + '/data/shippingMarks.json', function(data) {
        shippingMarksJson = data;

    })

    // datepicker
    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();
    });
    // init customer search dropdown
    customerInitSelect2($('select#custId'), []);
    $('select#custId').trigger("change")
    // proforma invoice table
    var table = $('#table-invoice-list').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/accounts/lc/search/proformaInvoice",
            data: function(data) {
                data.custId = $('#custId').find('option:selected').val();
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
            "data": "invoiceNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "invoiceNo"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "hsCode"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "date",
            "render": function(data, type, row) {
                return row.maker + ' / ' + row.model
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "firstRegDate"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "inspectionCertificateNo"
        }, {
            targets: 7,
            "className": "details-control",
            "data": "exportCertificateNo"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "fob",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0" data-v-min="0">' + ifNotValid(data, 0) + '</span>';
            }
        }, {
            targets: 9,
            "className": "details-control",
            "data": "insurance",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0" data-v-min="0">' + ifNotValid(data, 0) + '</span>';
            }
        }, {
            targets: 10,
            "className": "details-control",
            "data": "shipping",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0" data-v-min="0">' + ifNotValid(data, 0) + '</span>';
            }
        }, {
            targets: 11,
            "className": "details-control",
            "data": "freight",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0" data-v-min="0">' + ifNotValid(data, 0) + '</span>';
            }
        }, {
            targets: 12,
            "className": "details-control",
            "data": "total",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0" data-v-min="0">' + ifNotValid(data, 0) + '</span>';
            }
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init');

        },
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            var api = this.api(), data;
            var total = table.column(12, {
                page: 'current'
            }).data().reduce(function(a, b) {
                return a + ifNotValid(b, 0);
            }, 0);
            setAutonumericValue($('#table-invoice-list>tfoot>tr:first-child>th:eq(1)>span'), total);

        }

    });
    // search proforma invoice for the customer
    $('#btn-searchData').click(function() {
        table.ajax.reload()
    })
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('#btn-apply-filter').click(function() {
        table.draw();
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
    let fobCalc = function(element) {

        var closestEle = $(element).closest('.lc-items');

        var fob = Number(getAutonumericValue($(closestEle).find('input[name="fob"]')));
        var insurance = Number(getAutonumericValue($(closestEle).find('input[name="insurance"]')));
        var freight = Number(getAutonumericValue($(closestEle).find('input[name="freight"]')));
        var totalEle = $(closestEle).find('input[name="amountReceived"]');
        var total = Number(getAutonumericValue($(closestEle).find('input[name="amountReceived"]')));
        var totalWithoutFob = insurance + freight;
        fob = total - totalWithoutFob;

        if (fob < 0) {
            fob = Number(getAutonumericValue($(closestEle).find('input[name="fob"]')));
            total = fob + insurance + freight;
            alert($.i18n.prop('alert.profomainvoice.create.EqualsPrice'))
            setAutonumericValue($(element), 0)
            $(element).trigger('keyup');
            setAutonumericValue(totalEle, total);
            return;
        }
        var fobEle = $(closestEle).find('input[name="fob"]');
        setAutonumericValue($(fobEle), fob);

    }
    showTab(create_lc_step);
    var uniqueVessal;
    modal_create_lc.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        });

        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();
        }
        create_lc_step = 0;
        showTab(create_lc_step)
        let rowData = selectedRows.data().toArray();
        //         let itemTable = $(this).find('table#itemTable');
        //         let tableBody = itemTable.find('tbody');

        let consFlag = {};
        var consUnique = rowData.filter(function(data) {
            if (consFlag[data.consigneeId]) {
                return false;
            }
            consFlag[data.consigneeId] = true;
            return true;
        });

        let selectedConsigneeId;
        selectedConsigneeId = consUnique.length > 1 ? "" : consUnique[0].consigneeId

        $(this).find('select[name="consignee"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(consUnique, function(row) {
                return {
                    id: row.consigneeId,
                    text: row.consigneeName
                };
            })
        }).val(selectedConsigneeId).on('change', function() {
            let selectedConsignee = $(this).val();
            setConsigneeAddress(selectedConsignee, rowData);
        }).trigger('change');

        let npFlag = {};
        var npUnique = rowData.filter(function(data) {
            if (npFlag[data.consigneeId]) {
                return false;
            }
            npFlag[data.consigneeId] = true;
            return true;
        });

        let selectedNotifyPartyId;
        selectedNotifyPartyId = npUnique.length > 1 ? "" : npUnique[0].notifypartyId

        $(this).find('select[name="notifyParty"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(npUnique, function(row) {
                return {
                    id: row.notifypartyId,
                    text: row.notifypartyName
                };
            })
        }).val(selectedNotifyPartyId).on('change', function() {
            let selectednotifyParty = $(this).val();
            setNotifyPartyAddress(selectednotifyParty, rowData);
        }).trigger('change');

        $(this).find('textarea[name="shippingTerms"]').val('SHIPPING TERMS');
        $(this).find('input[name="proformaInvoiceId"]').val(rowData[0].invoiceNo);
        $(this).find('input[name="customerId"]').val(rowData[0].lcCustomerId);
        let clone = $('div#cloneElements>div.lc-items');
        let itemContainer = $(this).find('div#itemContainer');
        for (let i = 0; i < rowData.length; i++) {
            let item = clone.clone();
            item.find('input[name="stockNo"]').val(rowData[i].stockNo)
            item.find('input[name="chassisNo"]').val(rowData[i].chassisNo);
            item.find('input[name="maker"]').val(rowData[i].maker);
            item.find('input[name="model"]').val(rowData[i].model);
            item.find('input[name="proformaInvoiceId"]').val(rowData[i]["invoiceNo"]);
            item.find('input[name="hsCode"]').val(rowData[i].hsCode);
            setAutonumericValue(item.find('input[name="fob"]'), Number(ifNotValid(rowData[i].fob, 0)) + Number(ifNotValid(rowData[i].shipping, 0)))
            setAutonumericValue(item.find('input[name="insurance"]'), Number(ifNotValid(rowData[i].insurance, 0)));
            setAutonumericValue(item.find('input[name="freight"]'), Number(ifNotValid(rowData[i].freight, 0)));
            setAutonumericValue(item.find('input[name="amountReceived"]'), rowData[i].total);
            item.find('textarea[name="shippingMarks"]').val('AA JAPAN (PVT) LTD WEBSITE:WWW.AAJAPANCARS.COM E-MAIL INFO@AAJAPANCARS.COM TEL:0081-45-594-0507 FAX:0081-45-594-0508 MOBILE:0081-80-1094-0907');
            item.find('#shippingMarksName').select2({
                allowClear: true,
                width: '100%',
                data: $.map(shippingMarksJson, function(item) {
                    return {
                        id: item.marksId,
                        text: item.name,
                        data: item
                    };
                })
            })
            //lc-items

            //vessel details
            let vessel = findOneVesselDetailsByShippmentRequestId(rowData[i].shipmentRequestId);

            item.find('input[name="sailingDate"],input[name="bankSentDate"]').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true

            })
            if (!isEmpty(vessel.data)) {
                let scheduleText = vessel.data.shipName + ' [' + vessel.data.shippingCompanyName + ']' + ' [ ' + vessel.data.voyageNo + ' ]';
                item.find('input[name="scheduleText"]').val(scheduleText);
                item.find('input[name="vessel"]').val(vessel.data.shipName + " " + vessel.data.voyageNo);
                item.find('input[name="from"]').val(ifNotValid(vessel.data.orginPort, '') + '/' + ifNotValid(vessel.data.orginCountry, ''));
                item.find('input[name="to"]').val(ifNotValid(vessel.data.destinationPort, '') + '/' + ifNotValid(vessel.data.destinationCountry, ''));
                if (!isEmpty(vessel.data.etd)) {
                    let date = vessel.data.etd.split('-');
                    item.find('input[name="sailingDate"]').datepicker('setDate', new Date(date[2],date[1] - 1,date[0]));
                }
            }

            itemContainer.append(item)

        }
        $(this).find('select[name="customerId"]').each(function() {
            $('<option value="' + rowData[0].lcCustomerId + '">' + rowData[0].lcCustomerId + ' :: ' + rowData[0].lcCustFirstName + '(' + rowData[0].lcCustNickName + ')' + '</option>').appendTo(this);
        });
        customerInitSelect2($(this).find('select[name="customerId"]'), [], $('#modal-create-lc'))
        $(this).find('select[name="customerId"]').val(rowData[0].lcCustomerId).trigger("change");
        $(this).find('.autonumber ').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        $(this).find('div.form-step').css('display', 'none')
        $(this).find('input,textarea').val('');
        $(this).find('select').val('').trigger('change');
        $(this).find('div#itemContainer').html('');
        /* resetElementInput(this) */
    }).on('change', '#bank', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data.data)) {
            $('.beneficiaryCertify').val(data.data.beneficiaryCertify);
            $('.licenseDoc').val(data.data.licenseDoc);
        }
    }).on('click', '#btn-save', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!$('#update-lc-form').valid() || !$('#invoiceItemForm input[name="amountReceived"]').valid()) {
            return;
        }
        let total = $('div#itemContainer').find('div.lc-items input[name="amountReceived"]').toArray().reduce(function(a, b) {
            return Number(a) + Number(getAutonumericValue(b));
        }, 0);
        let lcAmount = Number(getAutonumericValue($('#modal-create-lc input[name="amount"]')))
        if (total != lcAmount) {
            alert("Total and LC amount mismatch.")
            return false;
        }
        let data = {};
        let stockDataArr = [];
        let stockData = {};
        $('div#itemContainer').find('div.lc-items').each(function(item) {
            stockData = {};
            stockData.scheduleText = $(this).find('input[name="scheduleText"]').val();
            stockData.stockNo = $(this).find('input[name="stockNo"]').val();
            stockData.chassisNo = $(this).find('input[name="chassisNo"]').val();
            stockData.maker = $(this).find('input[name="maker"]').val();
            stockData.model = $(this).find('input[name="model"]').val();
            stockData.hsCode = $(this).find('input[name="hsCode"]').val();
            stockData.customerId = $(this).find('select[name="customerId"]').val();
            stockData.vessel = $(this).find('input[name="vessel"]').val();
            stockData.from = $(this).find('input[name="from"]').val();
            stockData.to = $(this).find('input[name="to"]').val();
            stockData.sailingDate = $(this).find('input[name="sailingDate"]').val();
            stockData.bankSentDate = $(this).find('input[name="bankSentDate"]').val();
            stockData.shippingMarksId = $(this).find('select[name="shippingMarksName"]').val();
            stockData.proformaInvoiceId = $(this).find('input[name="proformaInvoiceId"]').val();
            stockData.shippingMarks = $(this).find('textarea[name="shippingMarks"]').val();

            stockData.fob = getAutonumericValue($(this).find('input[name="fob"]'));
            stockData.insurance = getAutonumericValue($(this).find('input[name="insurance"]'));
            stockData.freight = getAutonumericValue($(this).find('input[name="freight"]'));

            stockData.amount = getAutonumericValue($(this).find('input[name="amountReceived"]'));
            stockDataArr.push(stockData);
        });
        data.lcDetails = getFormData($('#update-lc-form').find('.form-control'));
        data.lcDetails.customerId = $('#update-lc-form').find('input[name="customerId"]').val()
        data.lcDetails.consigneeName = $('#update-lc-form').find('input[name="consigneeName"]').val()
        data.lcDetails.notifyPartyName = $('#update-lc-form').find('input[name="notifyPartyName"]').val()
        data.lcDetails.amount = getAutonumericValue($('#update-lc-form input[name="amount"]'));
        data.proformaInvoiceId = $('#update-lc-form').find('input[name="proformaInvoiceId"]').val();
        data.stockDataArr = stockDataArr;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/lc/create",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/accounts/lc/list?lcNo=' + data.data, '', 'GET');
                }

            }
        });

    }).on('click', '#btn-next-step', function() {
        let total = getAutonumericValue(modal_create_lc.find('input[name="amount"]'))
        setAutonumericValue($('#modal-create-lc span.totalAmountEntered'), total);

    }).on('change', '#shippingMarksName', function() {
        var data = $(this).closest('.lc-items').find('#shippingMarksName :selected').data('data');
        //.closest('lc-items');

        if (!isEmpty(data.data)) {
            $(this).closest('.lc-items').find('#shippingMarks').val(data.data.shippingMarks).trigger('change');

        } else if (isEmpty(data.data)) {
            $('#shippingMarks').val('').trigger('change');
        }
    }).on('keyup', 'input[name="insurance"],input[name="freight"]', function() {
        fobCalc(this);
    }).on('change', 'input[name="amountReceived"]', function() {
        fobCalc(this);
    })

})

function setConsigneeAddress(selectedConsignee, rowData) {
    rowData.forEach(function(row) {
        if (row.consigneeId == selectedConsignee) {
            $('#modal-create-lc textarea[name="cAddress"]').val(row.consigneeAddress);
            $('#modal-create-lc input[name="consigneeName"]').val(row.consigneeName);
        }
    });
}

function setNotifyPartyAddress(selectedNotifyParty, rowData) {
    rowData.forEach(function(row) {
        if (row.notifypartyId == selectedNotifyParty) {
            $('#modal-create-lc textarea[name="npAddress"]').val(row.notifypartyAddress);
            $('#modal-create-lc input[name="notifyPartyName"]').val(row.notifypartyName);
        }
    });
}
function customerInitSelect2(element, value, parent) {
    $(element).select2({
        allowClear: true,
        minimumInputLength: 2,
        dropdownParent: parent,
        ajax: {
            url: myContextPath + "/customer/getLcCustomer?flag=customer",
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
                $(this).empty();
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        },
        initSelection: function(element, callback) {
            callback({
                id: value.id,
                text: value.text
            });
        }
    })
}
// step forward / backward
function containerAllcationStepNextPrev(n) {
    // This function will figure out which tab to display
    var x = modal_create_lc.find('.form-step');
    // var x = document.getElementsByClassName("tab");
    // Exit the function if any field in the current tab is invalid:
    if (n == 1 && !$('form#update-lc-form').valid()) {
        return false;
    } else if (n == 1 && $('form#update-lc-form').valid()) {
        $(modal_create_lc.find('.step')[create_lc_step]).css('background-color', '#4CAF50');
    }

    // Hide the current tab:
    $(x[create_lc_step]).css('display', 'none');
    // Increase or decrease the current tab by 1:
    create_lc_step = create_lc_step + n;
    // if you have reached the end of the form... :
    if (create_lc_step >= x.length) {
        return false;
    }
    // Otherwise, display the correct tab:
    showTab(create_lc_step);
}
// multi step form
function showTab(n) {
    // This function will display the specified tab of the form ...
    var x = modal_create_lc.find('.form-step');
    $(x[n]).css('display', 'block');
    // ... and fix the Previous/Next buttons
    if (n == 0) {
        modal_create_lc.find('#btn-previous-step').css('display', 'none');

    } else {
        modal_create_lc.find('#btn-previous-step').css('display', 'inline');

    }
    if (n == (x.length - 1)) {
        modal_create_lc.find('#btn-next-step').css('display', 'none');
        modal_create_lc.find('#btn-previous-step,#btn-save').css('display', 'inline');
    } else {
        modal_create_lc.find('#btn-next-step').css('display', 'inline');
        modal_create_lc.find('#btn-save').css('display', 'none');
    }
    // ... and run a function that displays the correct step indicator:
    fixStepIndicator(n)
}
// Display the current tab
function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = modal_create_lc.find('span.step');
    // var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        $(x[i]).removeClass('active');
        // .css('background-color', '');
    }
    // ... and adds the "active" class to the current step:
    $(x[n]).addClass('active');

}
function findOneVesselDetailsByShippmentRequestId(shipmentRequestId) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",

        async: false,
        url: myContextPath + '/shipping/vessalsDetails?shipmentRequestId=' + shipmentRequestId,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
