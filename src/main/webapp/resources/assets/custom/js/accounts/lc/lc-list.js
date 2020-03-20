$(function() {
    let shippingMarksJson;
    // Shipping Marks Filter
    $.getJSON(myContextPath + '/data/shippingMarks.json', function(data) {
        shippingMarksJson = data;

    })
    //filter By Customer Search
    $('#custselectId').select2({
        allowClear: true,
        placeholder: 'Search customer email',
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
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    }).on('change', function(event) {
        filterCustomer = $(this).find('option:selected').val();
        var customersFilter = $("input[name='radioShowTable']:checked").val();
        if (customersFilter == 0) {
            tableStocks.draw();
        } else if (customersFilter == 1) {
            table.draw();
        }

        table.draw();
    })
    //yen bank
    $.getJSON(myContextPath + '/data/bankOnCurrencyType.json' + "?currencyType=1", function(data) {
        $('div#modal-apply-lc select[name="yenBank"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        })
    })
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
    $.getJSON(myContextPath + '/data/foreignBanks.json', function(data) {
        bankJson = data;
        $('div#modal-edit-lc select#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankId,
                    text: item.bank,
                    data: item
                };
            })
        })

        bankFilterJson = data;
        $('#bankFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankFilterJson, function(item) {
                return {
                    id: item.bankId,
                    text: item.bank,
                    data: item
                };
            })
        })
    })

    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            table.column(6).visible(false);
            table.column(7).visible(false);
            $('#table-invoice-list').find('tbody').find('tr td').find('a.billOfExchange').removeClass('hidden');
            table.ajax.reload();
        } else if ($(this).val() == 1) {
            table.column(6).visible(true);
            table.column(7).visible(true);
            $('#table-invoice-list').find('tbody').find('tr td').find('a.billOfExchange').addClass('hidden');
            table.ajax.reload();
        }
    })
    $('div#modal-apply-lc select[name="methodOfProcess"]').select2({
        allowClear: true,
        width: '100%'
    })

    var table = $('#table-invoice-list').DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/accounts/lc/invoice/list",
            data: function(data) {
                data.lcNo = $('#lcNo').val();
                data.flag = $('input[type="radio"]:checked').val();
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
            "visible": false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" name="id" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "lcNo"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "customerName"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "issueDate"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "expiryDate"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "bank"
        }, {
            targets: 6,
            "className": "details-control dt-right",
            "data": "amount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "details-control",
            'visible': false,
            "data": "billOfExchangeNo"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "consignee"
        }, {
            targets: 9,
            "className": "details-control",
            "data": "cAddress"
        }, {
            targets: 10,
            "className": "details-control",
            "data": "notifyParty"
        }, {
            targets: 11,
            "className": "details-control",
            "data": "npAddress"
        }, {
            targets: 12,
            "className": "align-center",
            "data": "lcNo",
            "width": "80px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var actionHtml = '';
                if (type === 'display') {
                    let flag = $('input[name=radioReceivedFilter]:checked').val();
                    if (flag == 0) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs billOfExchange" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-apply-lc"><i class="fa fa-fw fa-check"></i></a>';
                        actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-lc"><i class="fa fa-fw fa-pencil"></i></a>'
                    } else if (flag == 1) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modalUpdateDHL"><i class="fa fa-fw fa-pencil"></i></a>'
                    }
                    actionHtml += '<a href="' + myContextPath + '/accounts/lc/invoiceDetail/' + row.lcInvoiceNo + '.pdf" class="ml-5 btn btn-default btn-xs invoice-pdf"><i class="fa fa-fw  fa-download "></i></a>'
                    return actionHtml;
                }
                return data;
            }
        }, {
            targets: 13,
            "data": "customerId",
            "searchable": true,
            "visible": false,
        }, {
            targets: 14,
            "data": "bankId",
            "searchable": true,
            "visible": false,
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init');

        }

    });
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    });
    let issueDateFilter, expiryDateFilter;
    $('input[name="issueDateFilter"]').on('change', function() {
        issueDateFilter = $(this).val();
        table.draw();
    })
    $('input[name="expiryDateFilter"]').on('change', function() {
        expiryDateFilter = $(this).val();
        table.draw();
    })
    $('input[name="sailingDate"], input[name="bankSentDate"]').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {//$(this).valid();

    });
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
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
    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            showDetails(tr);
        }

    });
    function showDetails(tr) {
        // var tr = $(this).closest('tr');
        var row = table.row(tr)
        table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }
        })
        var detailsElement = format(row.data());
        row.child(detailsElement).show();
        detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
        tr.addClass('shown');
        // tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    }
    //modal apply lc
    let modalLcApply = $('div#modal-apply-lc')
    let modalLcApplyInvoker;
    modalLcApply.on('show.bs.modal', function(e) {
        modalLcApplyInvoker = $(e.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select,textarea').val('').trigger('change');
    }).on('click', 'button#btn-save', function(event) {
        let confirmLcBtn = modalLcApplyInvoker;
        if (!modalLcApply.find('form#formLcApply').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let row = table.row(confirmLcBtn.closest('tr'));
        let items = [];
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked');
            if (selectedRow.length > 0) {
                $.each(selectedRow, function(item) {
                    let rowData = {};
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data)
                    // rowData['stockNo'] = data.stockNo;
                    items.push(data.stockNo);
                })
            } else {
                let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                $.each(selectedRow, function(item) {
                    let rowData = {};
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data)
                    // rowData['stockNo'] = data.stockNo;
                    items.push(data.stockNo);
                })
            }
        } else {
            showDetails(confirmLcBtn.closest('tr'));
            // var row = table.row(confirmLcBtn.closest('tr'));
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let rowData = {};
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                // rowData['stockNo'] = data.stockNo;
                items.push(data.stockNo);
            })
        }
        let rowData = row.data();
        let data = {};
        //  data['lcInvoiceNo'] = rowData.lcInvoiceNo;
        data['stockNos'] = items;
        data["methodOfProcess"] = modalLcApply.find('select[name="methodOfProcess"]').val();
        data["yenBank"] = modalLcApply.find('select[name="yenBank"]').val();
        let queryString = "?lcInvoiceNo=" + rowData.lcInvoiceNo + "&proformaInvoiceId=" + rowData.proformaInvoiceId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/lc/update/billOfExchange" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    modalLcApply.modal('toggle');
                }
            }
        })
    });
    //modal update dhlNo
    let modalUpdateDHL = $('div#modalUpdateDHL')
    let modalUpdateDHLInvoker;
    modalUpdateDHL.on('show.bs.modal', function(e) {
        modalUpdateDHLInvoker = $(e.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select,textarea').val('').trigger('change');
    }).on('click', 'button#btn-save', function(event) {
        let confirmLcBtn = modalUpdateDHLInvoker;
        if (!modalUpdateDHL.find('form#formUpdateDHL').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let row = table.row(confirmLcBtn.closest('tr'));
        let data = {};
        data["dhlNo"] = modalUpdateDHL.find('input[name="dhlNo"]').val();

        let rowData = row.data();
        let queryString = "?lcInvoiceNo=" + rowData.lcInvoiceNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/lc/update/dhlNo" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    modalUpdateDHL.modal('toggle');
                }
            }
        })
    });

    //edit lc
    let modal_edit_lc = $('#modal-edit-lc');
    modal_edit_lc.find('#itemContainer').slimScroll({
        start: 'bottom',
        height: '500px',
        railVisible: true,
    });

    let create_lc_step = 0;
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
    modal_edit_lc.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var selectedRows = table.row(targetElement.closest('tr'));

        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();
        }
        create_lc_step = 0;
        showTab(create_lc_step)
        let rowData = selectedRows.data();
        modal_edit_lc.find('select#searchProforma').empty();
        $.get({
            url: myContextPath + "/accounts/lc/search/customer/proformaInvoice?custId=" + rowData.customerId,
            async: false
        }, function(data) {

            modal_edit_lc.find('select#searchProforma').select2({
                allowClear: true,
                width: '100%',
                data: $.map(data, function(item) {
                    return {
                        id: item.invoiceNo,
                        text: item.invoiceNo + " [" + item.chassisNo + "]",
                        data: item
                    };
                })
            })

        });
        modal_edit_lc.find('select#searchProforma').val('').trigger('change');
        $(this).find('input[name="lcInvoiceNo"]').val(rowData.lcInvoiceNo);
        $(this).find('input[name="consignee"]').val(rowData.consignee);
        $(this).find('input[name="notifyParty"]').val(rowData.notifyParty);
        $(this).find('textarea[name="npAddress"]').val(rowData.npAddress);
        $(this).find('textarea[name="cAddress"]').val(rowData.cAddress);
        $(this).find('input[name="lcNo"]').val(rowData.lcNo);
        $(this).find('select[name="bankId"]').val(rowData.bankId).trigger('change');
        $(this).find('input[name="issueDate"]').val(rowData.issueDate);
        $(this).find('input[name="expiryDate"]').val(rowData.expiryDate);
        setAutonumericValue($(this).find('input[name="amount"]'), rowData.amount);
        $(this).find('select[name="shippingTermsName"]').val(rowData.shippingTermsName).trigger('change');
        $(this).find('textarea[name="shippingTerms"]').val(rowData.shippingTerms);
        $(this).find('textarea[name="beneficiaryCertify"]').val(rowData.beneficiaryCertify);
        $(this).find('textarea[name="licenseDoc"]').val(rowData.licenseDoc);

        $(this).find('input[name="customerId"]').val(rowData.customerId);
        let clone = $('div#cloneElements>div.lc-items');
        let itemContainer = $(this).find('div#itemContainer');
        for (let i = 0; i < rowData.items.length; i++) {
            let item = clone.clone();
            item.find('input[name="stockNo"]').val(rowData.items[i]["stockNo"])
            item.find('input[name="chassisNo"]').val(rowData.items[i]["chassisNo"]);
            item.find('input[name="proformaInvoiceNo"]').val(rowData.items[i]["proformaInvoiceNo"]);
            item.find('input[name="maker"]').val(rowData.items[i]["maker"]);
            item.find('input[name="model"]').val(rowData.items[i]["model"]);
            setAutonumericValue(item.find('input[name="fob"]'), Number(ifNotValid(rowData.items[i].fob, 0)) + Number(ifNotValid(rowData.items[i].shipping, 0)))
            setAutonumericValue(item.find('input[name="insurance"]'), Number(ifNotValid(rowData.items[i].insurance, 0)));
            setAutonumericValue(item.find('input[name="freight"]'), Number(ifNotValid(rowData.items[i].freight, 0)));
            setAutonumericValue(item.find('input[name="amountReceived"]'), rowData.items[i].amount);
            item.find('input[name="hsCode"]').val(rowData.items[i]["hsCode"]);
            item.find('textarea[name="shippingMarks"]').val(rowData.items[i]["shippingMarks"]);
            item.find('input[name="scheduleText"]').val(rowData.items[i]["schedule"]);
            item.find('input[name="vessel"]').val(rowData.items[i]["perVessel"]);
            item.find('input[name="from"]').val(rowData.items[i]["from"]);
            item.find('input[name="to"]').val(rowData.items[i]["to"]);
            item.find('input[name="sailingDate"]').val(rowData.items[i]["sailingDate"]);
            item.find('input[name="proformaInvoiceId"]').val(rowData.items[i]["proformaInvoiceId"]);
            item.find('button.btn-remove-item').attr('data-flag', 1);
            item.find('input[name="isNew"]').val('false')
            if (!isEmpty(rowData.items[i]["customerId"])) {
                $.get({
                    url: myContextPath + "/customer/data/" + rowData.items[i]["customerId"],
                    async: false
                }, function(data) {
                    data = data.data;
                    let select = item.find('select[name="customerId"]');
                    let option = $('<option value="' + data.code + '">' + data.code + ' :: ' + data.firstName + '(' + data.nickName + ')</option>');
                    option.appendTo(select);
                    customerInitSelect2(select, [], modal_edit_lc)
                    select.val(data.code).trigger("change");

                });
            }
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
            item.find('#shippingMarksName').val(rowData.items[i]["shippingMarksId"]).trigger('change');
            item.find('#shippingMarks').val(rowData.items[i]["shippingMarks"]);
            item.find('input[name="sailingDate"],input[name="bankSentDate"]').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true

            })
            itemContainer.append(item)

        }

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
    }).on('click', 'button#btn-add', function() {
        let proforma = modal_edit_lc.find('select#searchProforma');
        if (!isEmpty(proforma.val())) {
            let option = proforma.find(':selected');
            var data = option.data('data');
            option.prop('disabled', true);
            if (!isEmpty(data) && !isEmpty(data.data)) {
                data = data.data;
                let clone = $('div#cloneElements>div.lc-items');
                let itemContainer = modal_edit_lc.find('div#itemContainer');

                let item = clone.clone();
                item.attr('data-id', data.invoiceNo);
                item.find('input[name="stockNo"]').val(data.stockNo)
                item.find('input[name="chassisNo"]').val(data.chassisNo);
                item.find('input[name="maker"]').val(data.maker);
                item.find('input[name="model"]').val(data.model);
                item.find('input[name="hsCode"]').val(data.hsCode);
                item.find('input[name="proformaInvoiceId"]').val(data["invoiceNo"]);
                item.find('input[name="proformaInvoiceNo"]').val(data["invoiceNo"]);
                item.find('textarea[name="shippingMarks"]').val('AA JAPAN (PVT) LTD WEBSITE:WWW.AAJAPANCARS.COM E-MAIL INFO@AAJAPANCARS.COM TEL:0081-45-594-0507 FAX:0081-45-594-0508 MOBILE:0081-80-1094-0907');
                item.find('button.btn-remove-item').attr('data-flag', 0);
                item.find('input[name="isNew"]').val('true')
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

                setAutonumericValue(item.find('input[name="fob"]'), Number(ifNotValid(data.fob, 0)) + Number(ifNotValid(data.shipping, 0)))
                setAutonumericValue(item.find('input[name="insurance"]'), Number(ifNotValid(data.insurance, 0)));
                setAutonumericValue(item.find('input[name="freight"]'), Number(ifNotValid(data.freight, 0)));
                setAutonumericValue(item.find('input[name="amountReceived"]'), data.total);
                //vessel details
                let vessel = findOneVesselDetailsByShippmentRequestId(data.shipmentRequestId);

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

                $(item).find('select[name="customerId"]').each(function() {
                    $('<option value="' + data.lcCustomerId + '">' + data.lcCustomerId + ' :: ' + data.lcCustFirstName + '(' + data.lcCustNickName + ')' + '</option>').appendTo(this);
                });
                customerInitSelect2($(item).find('select[name="customerId"]'), [], modal_edit_lc)
                $(item).find('select[name="customerId"]').val(data.lcCustomerId).trigger("change");
                item.find('input[name="sailingDate"],input[name="bankSentDate"]').datepicker({
                    format: "dd-mm-yyyy",
                    autoclose: true

                })
                $(item).find('.autonumber ').autoNumeric('init');
            }
        }

    }).on('click', 'button.btn-remove-item', function() {
        let flag = $(this).attr('data-flag');
        let container = $(this).closest('.lc-items');
        let length = $('div#itemContainer>.lc-items:visible').length;
        if (length > 1 && flag == 1) {
            container.find('input[name="isCancelled"]').val('true');
            container.hide();
        } else if (length > 1 && flag == 0) {
            let val = container.attr('data-id');
            $('select#searchProforma option[value="' + val + '"]').prop('disabled', false);
            container.remove();
        }
    }).on('click', '#btn-save', function() {

        if (!modal_edit_lc.find('#update-lc-form').valid() || !modal_edit_lc.find('#invoiceItemForm input[name="amountReceived"]').valid()) {
            return;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        let total = modal_edit_lc.find('div#itemContainer').find('div.lc-items:visible input[name="amountReceived"]').toArray().reduce(function(a, b) {
            return Number(a) + Number(getAutonumericValue(b));
        }, 0);
        let lcAmount = Number(getAutonumericValue(modal_edit_lc.find('input[name="amount"]')))
        if (total != lcAmount) {
            alert("Total and LC amount mismatch.")
            return false;
        }
        let data = {};
        let stockDataArr = [];
        let stockData = {};
        modal_edit_lc.find('div#itemContainer').find('div.lc-items').each(function(item) {
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
            stockData.shippingMarks = $(this).find('textarea[name="shippingMarks"]').val();

            stockData.fob = getAutonumericValue($(this).find('input[name="fob"]'));
            stockData.insurance = getAutonumericValue($(this).find('input[name="insurance"]'));
            stockData.freight = getAutonumericValue($(this).find('input[name="freight"]'));
            stockData.amount = getAutonumericValue($(this).find('input[name="amountReceived"]'));
            stockData.proformaInvoiceId = $(this).find('input[name="proformaInvoiceId"]').val();
            stockData.proformaInvoiceNo = $(this).find('input[name="proformaInvoiceNo"]').val();
            stockData.isCancelled = $(this).find('input[name="isCancelled"]').val();
            stockData.isNew = $(this).find('input[name="isNew"]').val();
            stockDataArr.push(stockData);
        });
        data.lcDetails = getFormData(modal_edit_lc.find('#update-lc-form').find('.form-control'));
        data.lcDetails.customerId = modal_edit_lc.find('#update-lc-form').find('input[name="customerId"]').val()
        data.lcDetails.consigneeName = modal_edit_lc.find('#update-lc-form').find('input[name="consigneeName"]').val()
        data.lcDetails.notifyPartyName = modal_edit_lc.find('#update-lc-form').find('input[name="notifyPartyName"]').val()
        data.lcDetails.amount = getAutonumericValue(modal_edit_lc.find('#update-lc-form input[name="amount"]'));
        data.lcDetails.lcInvoiceNo = modal_edit_lc.find('#update-lc-form input[name="lcInvoiceNo"]').val();
        data.proformaInvoiceId = modal_edit_lc.find('#update-lc-form').find('input[name="proformaInvoiceId"]').val();
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
            url: myContextPath + "/accounts/lc/edit",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    modal_edit_lc.modal('toggle');

                }

            }
        });

    }).on('click', '#btn-next-step', function() {
        let total = getAutonumericValue(modal_edit_lc.find('input[name="amount"]'))
        setAutonumericValue(modal_edit_lc.find('span.totalAmountEntered'), total);

    }).on('change', '#shippingMarksName', function() {
        var data = $(this).closest('.lc-items').find('#shippingMarksName :selected').data('data');
        //.closest('lc-items');

        if (!isEmpty(data) && !isEmpty(data.data)) {
            $(this).closest('.lc-items').find('#shippingMarks').val(data.data.shippingMarks).trigger('change');

        } else {
            $('#shippingMarks').val('').trigger('change');
        }
    }).on('change', 'select#shippingTermsName', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data.data)) {
            modal_edit_lc.find('select#shippingTerms').val(data.data.shippingTerms).trigger('change');

        } else {
            modal_edit_lc.find('select#shippingTerms').val('').trigger('change');
        }
    }).on('click', 'button#btn-next-step', function() {
        nextPrev(1)
    }).on('click', 'button#btn-previous-step', function() {
        nextPrev(-1)
    }).on('keyup', 'input[name="insurance"],input[name="freight"]', function() {
        fobCalc(this);
    }).on('change', 'input[name="amountReceived"]', function() {
        fobCalc(this);
    })
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
    // step forward / backward
    function nextPrev(n) {
        // This function will figure out which tab to display
        var x = modal_edit_lc.find('.form-step');
        // var x = document.getElementsByClassName("tab");
        // Exit the function if any field in the current tab is invalid:
        if (n == 1 && !$('form#update-lc-form').valid()) {
            return false;
        } else if (n == 1 && $('form#update-lc-form').valid()) {
            $(modal_edit_lc.find('.step')[create_lc_step]).css('background-color', '#4CAF50');
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
        var x = modal_edit_lc.find('.form-step');
        $(x[n]).css('display', 'block');
        // ... and fix the Previous/Next buttons
        if (n == 0) {
            modal_edit_lc.find('#btn-previous-step').css('display', 'none');

        } else {
            modal_edit_lc.find('#btn-previous-step').css('display', 'inline');

        }
        if (n == (x.length - 1)) {
            modal_edit_lc.find('#btn-next-step').css('display', 'none');
            modal_edit_lc.find('#btn-previous-step,#btn-save').css('display', 'inline');
        } else {
            modal_edit_lc.find('#btn-next-step').css('display', 'inline');
            modal_edit_lc.find('#btn-save').css('display', 'none');
        }
        // ... and run a function that displays the correct step indicator:
        fixStepIndicator(n)
    }
    // Display the current tab
    function fixStepIndicator(n) {
        // This function removes the "active" class of all steps...
        var i, x = modal_edit_lc.find('span.step');
        // var i, x = document.getElementsByClassName("step");
        for (i = 0; i < x.length; i++) {
            $(x[i]).removeClass('active');
            // .css('background-color', '');
        }
        // ... and adds the "active" class to the current step:
        $(x[n]).addClass('active');

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
    //update lc details
    var modalTriggerBtnEle;
    $('#modal-lc-update').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerBtnEle = $(event.relatedTarget);
        var tr = $(modalTriggerBtnEle).closest('tr');
        var rowData = table.row(tr).data();
        $(this).find('input[name="consigneeName"]').val(rowData.consignee);
        $(this).find('input[name="notifyPartyName"]').val(rowData.notifyParty);
        $(this).find('textarea[name="npAddress"]').val(rowData.npAddress);
        $(this).find('textarea[name="cAddress"]').val(rowData.cAddress);
        $(this).find('textarea[name="shippingTerms"]').val(rowData.shippingTerms);
        $(this).find('textarea[name="beneficiaryCertify"]').val(rowData.beneficiaryCertify);
        $(this).find('textarea[name="licenseDoc"]').val(rowData.licenseDoc);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-save-lc', function(event) {
        var data = {};
        var tr = $(modalTriggerBtnEle).closest('tr');
        var row = table.row(tr);
        var rowData = table.row(tr).data();
        var id = rowData.id;

        var data = {};
        data.consignee = $('#modal-lc-update').find('input[name="consigneeName"]').val();
        data.notifyparty = $('#modal-lc-update').find('input[name="notifyPartyName"]').val();
        data.cAddress = $('#modal-lc-update').find('textarea[name="cAddress"]').val();
        data.npAddress = $('#modal-lc-update').find('textarea[name="npAddress"]').val();

        data.shippingTerms = $('#modal-lc-update').find('textarea[name="shippingTerms"]').val();
        data.beneficiaryCertify = $('#modal-lc-update').find('textarea[name="beneficiaryCertify"]').val();
        data.licenseDoc = $('#modal-lc-update').find('textarea[name="licenseDoc"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/lc/invoice/update?id=" + id,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    //                     row.data(data.data).invalidate();
                    table.ajax.reload();
                    $('#modal-lc-update').modal('toggle');

                }

            }
        });

    })

    //update lc item details
    var modalTriggerBtnEle1;
    $('#modal-lc-item-update').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerBtnEle1 = $(event.relatedTarget);
        var tr = $(modalTriggerBtnEle1).closest('tr');
        let data = JSON.parse(tr.attr('data-json'));
        $(this).find('input[name="maker"]').val(ifNotValid(data.maker, ''));
        $(this).find('input[name="model"]').val(ifNotValid(data.model, ''));
        $(this).find('input[name="hsCode"]').val(ifNotValid(data.hsCode, ''));
        $(this).find('input[name="invoiceId"]').val(ifNotValid(data.invoiceId, ''));
        $(this).find('input[name="perVessel"]').val(ifNotValid(data.perVessel, ''));
        $(this).find('input[name="from"]').val(ifNotValid(data.from, ''));
        $(this).find('input[name="to"]').val(ifNotValid(data.to, ''));
        $(this).find('textarea[name="shippingMarks"]').val(ifNotValid(data.shippingMarks, ''));
        $(this).find('input[name="bankSentDate"]').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true

        })
        if (!isEmpty(data.bankSentDate)) {
            let date = data.bankSentDate.split('-');
            $(this).find('input[name="bankSentDate"]').datepicker('setDate', new Date(date[2],date[1] - 1,date[0]));
        }

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-save-lc-item', function(event) {
        var data = {};
        var id = $('#modal-lc-item-update').find('input[name="invoiceId"]').val();
        data.maker = $('#modal-lc-item-update').find('input[name="maker"]').val();
        data.model = $('#modal-lc-item-update').find('input[name="model"]').val();
        data.hsCode = $('#modal-lc-item-update').find('input[name="hsCode"]').val();
        data.perVessel = $('#modal-lc-item-update').find('input[name="perVessel"]').val();
        data.from = $('#modal-lc-item-update').find('input[name="from"]').val();
        data.to = $('#modal-lc-item-update').find('input[name="to"]').val();
        data.bankSentDate = $('#modal-lc-item-update').find('input[name="bankSentDate"]').val();
        data.shippingMarks = $('#modal-lc-item-update').find('textarea[name="shippingMarks"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/lc/invoiceItem/update?id=" + id,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    $('#modal-lc-item-update').modal('toggle');
                }

            }
        });

    })

    var filterCustomer, filterStaff;
    $('#custId').on('change', function() {
        filterCustomer = $(this).find('option:selected').val();
        table.draw();
    })

    $('#select_staff').on('change', function() {
        filterStaff = $(this).find('option:selected').val();
        table.draw();
    });

    //     $('#bank').change(function() {
    //         var selectedVal = $(this).find('option:selected').val();

    //         filterBank = $('#bank').val();
    //         table.draw();
    //     });
    $('#bankFilter').change(function() {
        var selectedBank = $(this).find('option:selected').val();

        filterForeignBank = $('#bankFilter').val();
        table.draw();
    });

    var filterBank = $('#bank').find('option:selected').val();
    var filterForeignBank = $('#bankFilter').find('option:selected').val();
    var filterCustomer = $('#custselectId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //filter
        if (typeof issueDateFilter != 'undefined' && issueDateFilter.length != '') {
            if (aData[3].length == 0 || aData[3] != issueDateFilter) {
                return false;
            }
        }
        if (typeof expiryDateFilter != 'undefined' && expiryDateFilter.length != '') {
            if (aData[4].length == 0 || aData[4] != expiryDateFilter) {
                return false;
            }
        }

        //orgin port filter

        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[13].length == 0 || aData[13] != filterCustomer) {
                return false;
            }
        }

        if (typeof filterBank != 'undefined' && filterBank.length != '') {
            if (aData[14].length == 0 || aData[14] != filterBank) {
                return false;
            }
        }
        if (typeof filterForeignBank != 'undefined' && filterForeignBank.length != '') {
            if (aData[14].length == 0 || aData[14] != filterForeignBank) {
                return false;
            }
        }
        return true;
    })

})
function format(data) {
    var element = $('#clone-element-container').find('.invoice-item-container').clone();
    var row = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < data.items.length; i++) {
        //         $(element).find('table>tbody').find('.amount').find('span.autonumber').autoNumeric('init');
        var rowEle = $(row).clone();
        rowEle.attr('data-json', JSON.stringify(data.items[i]));
        // $(rowEle).find('td.s-no>span').html(i + 1);
        $(rowEle).find('td.s-no>input[name="maker"]').val(ifNotValid(data.items[i].maker, ''));
        $(rowEle).find('td.s-no>input[name="model"]').val(ifNotValid(data.items[i].model, ''));
        $(rowEle).find('td.s-no>input[name="hsCode"]').val(ifNotValid(data.items[i].hsCode, ''));
        $(rowEle).find('td.s-no>input[name="invoiceId"]').val(ifNotValid(data.items[i].invoiceId, ''));
        $(rowEle).find('td.s-no>input[name="shippingTerms"]').val(ifNotValid(data.items[i].shippingTerms, ''));
        $(rowEle).find('td.s-no>input[name="shippingMarks"]').val(ifNotValid(data.items[i].shippingMarks, ''));
        $(rowEle).find('td.s-no>input[name="perVessel"]').val(ifNotValid(data.items[i].perVessel, ''));
        $(rowEle).find('td.s-no>input[name="from"]').val(ifNotValid(data.items[i].from, ''));
        $(rowEle).find('td.s-no>input[name="to"]').val(ifNotValid(data.items[i].to, ''));
        $(rowEle).find('td.s-no>input[name="sailingDate"]').val(ifNotValid(data.items[i].sailingDate, ''));
        $(rowEle).find('td.s-no>input[name="schedule"]').val(ifNotValid(data.items[i].schedule, ''));

        $(rowEle).find('td.stockNo').html(ifNotValid(data.items[i].stockNo, ''));
        $(rowEle).find('td.chassisNo').html(ifNotValid(data.items[i].chassisNo, ''));
        $(rowEle).find('td.maker').html(ifNotValid(data.items[i].maker, ''));
        $(rowEle).find('td.model').html(ifNotValid(data.items[i].model, ''));
        $(rowEle).find('td.hsCode').html(ifNotValid(data.items[i].hsCode, ''));
        $(rowEle).find('td.inspectionCertificateNo').html(ifNotValid(data.items[i].inspectionCertificateNo, ''));
        $(rowEle).find('td.exportCertificateNo').html(ifNotValid(data.items[i].exportCertificateNo, ''));

        $(rowEle).find('td.fob>span').html(ifNotValid(data.items[i].fob, ''));
        $(rowEle).find('td.insurance>span').html(ifNotValid(data.items[i].insurance, ''));
        $(rowEle).find('td.freight>span').html(ifNotValid(data.items[i].freight, ''));

        $(rowEle).find('td.amount>span').html(Number(ifNotValid(data.items[i].amount, 0)));
        $(rowEle).find('td.customerId').html(ifNotValid(data.items[i].customerName, ''));
        $(rowEle).removeClass('hide');
        $(element).find('table>tbody').append(rowEle);
    }
    return element;
}

function setConsigneeAddress(selectedConsignee, rowData) {
    rowData.forEach(function(row) {
        if (row.proConsigneeId == selectedConsignee) {
            $('#modal-lc-update textarea[name="cAddress"]').val(row.consigneeAddress);
        }
    });
}

function setNotifyPartyAddress(selectedNotifyParty, rowData) {
    rowData.forEach(function(row) {
        if (row.proNotifypartyId == selectedNotifyParty) {
            $('#modal-lc-update textarea[name="npAddress"]').val(row.notifypartyAddress);
        }
    });
}
function findAllVessalByScheduleId(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        data: data,
        async: false,
        url: myContextPath + '/shipping/vessalsByScheduleIds.json',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
//validation
$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('div#modal-apply-lc form#formLcApply').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'methodOfProcess': 'required',
            'yenBank': 'required'
        }
    });
    $('div#modal-edit-lc form#update-lc-form').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'lcNo': 'required',
            'bankId': 'required',
            'issueDate': 'required',
            'expiryDate': 'required',
            'amount': 'required',
        }
    });
    $('div#modal-edit-lc form#invoiceItemForm').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'amountReceived': 'required'
        }
    });
    $('div#modalUpdateDHL form#formUpdateDHL').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'dhlNo': 'required'
        }
    });
})
