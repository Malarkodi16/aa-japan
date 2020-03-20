$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    var custIdElem = $('#custId');
    customerInitSelect2(custIdElem, []);
    custIdElem.trigger("change")
    $.getJSON(myContextPath + "/data/foreignBanks.json", function(data) {

        $('#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.bankId,
                    text: item.bank
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/user/getRoleSales", function(data) {
        var salesJson = data;
        var ele = $('#select_sales_staff');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                var staff = item.username + +(item.userId)
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    var table = $('#table-invoice-list').DataTable({
        "dom": '<<t>ip>',
        "pageLength" : 25,
        "ajax": {
            url: myContextPath + "/accounts/lc/proformaInvoice/list",
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
            "data": "date"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "firstName",
            "render": function(data, type, row) {
                return row.firstName + ' ' + row.lastName
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "firstName",
            "render": function(data, type, row) {
                return row.cFirstName + ' ' + row.cLastName
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "firstName",
            "render": function(data, type, row) {
                return row.npFirstName + ' ' + row.npLastName
            }
        }, {
            targets: 6,
            "className": "details-control",
            "data": "paymentType"
        }]

    });

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
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            addListValidations($(this.node()));
        })

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
        table.rows({
            selected: false
        }).every(function(rowIdx, tableLoop, rowLoop) {
            removeListValidations($(this.node()));
        })

    });
    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            var rowData = row.data();
            row.child(format(rowData)).show();
            var childRowElement = row.child();
            var custometDropdownEles = $(childRowElement).find('select[name="itemCustId"].select2');
            $(custometDropdownEles).each(function() {
                $('<option value="' + rowData.customerId + '">' + rowData.firstName + ' ' + rowData.lastName + '</option>').appendTo(this);
            });
            customerInitSelect2(custometDropdownEles, {
                id: rowData.customerId,
                text: rowData.customerId + ' :: ' + rowData.firstName + ' ' + rowData.lastName
            });

            custometDropdownEles.val(rowData.customerId).trigger("change");

            tr.addClass('shown');
            /*numbers*/
            $('#invoice-item-details').find('.autonumber').autoNumeric('init');
            $('span.autonumber').autoNumeric('init');
        }
    });
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    $('#btn-searchData').click(function() {
        table.ajax.reload()
    })

    var modalTriggerBtnEle;
    var invoiceItemContainerEle;
    var selectedRowsEle;
    $('#modal-lc-update').on('show.bs.modal', function(event) {

        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerBtnEle = $(event.relatedTarget);
        invoiceItemContainerEle = $(modalTriggerBtnEle).closest('.invoice-item-container');
        selectedRowsEle = $(invoiceItemContainerEle).find('table>tbody>tr').find('td:first-child').find('input:checked').closest('tr');
        if (selectedRowsEle.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();
        }
        var a = amountValid($(selectedRowsEle));
        var enterAmount = $(selectedRowsEle).find('input[name="amount"]').val();
        if (a == false) {
            alert($.i18n.prop('alert.profomainvoice.create.EqualsPrice'));
            return event.preventDefault();
        }

        if (!isEntryValid($(selectedRowsEle))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return event.preventDefault();
        }
        /*numbers*/
          $('#invoice-item-details').find('.autonumber').autoNumeric('init');
        $('#update-lc-form').find('.autonumber').autoNumeric('init').autoNumeric("set", enterAmount).prop('readonly', true);

    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('select').val('').trigger('change');
        /*resetElementInput(this)*/
    }).on("click", '#btn-save-lc', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        if (!$('#update-lc-form').valid()) {
            return;
        }
        var data = {};
        var stockDataArr = [];
        var stockData = {};
        autoNumericSetRawValue($('#invoice-item-details').find('input[name="amount"]'))
        autoNumericSetRawValue($('#update-lc-form').find('input[name="amount"]'))
        $(selectedRowsEle).each(function() {
            stockData = {};
            stockData.stockNo = $(this).find('td:first-child').find('input[name="stockNo[]"]').val();
            stockData.chassisNo = $(this).find('td:first-child').find('input[name="chassisNo"]').val();
            stockData.maker = $(this).find('td:first-child').find('input[name="maker"]').val();
            stockData.model = $(this).find('td:first-child').find('input[name="model"]').val();
            stockData.hsCode = $(this).find('td:first-child').find('input[name="hsCode"]').val();
            stockData.customerId = $(this).find('select[name="itemCustId"]').find('option:selected').val();
            stockData.amount = $(this).find('input[name="amount"]').val();
            stockDataArr.push(stockData);
        });
        var data = {};
        data.lcDetails = getFormData($(this).closest('.modal-content').find('input,select,textarea'));
        data.proformaInvoiceId = $(invoiceItemContainerEle).find('input[name="proformaInvoiceId"]').val();
        data.cAddress = $(invoiceItemContainerEle).find('input[name="cAddress"]').val();
        data.npAddress = $(invoiceItemContainerEle).find('input[name="npAddress"]').val();
        data.customerName = $(invoiceItemContainerEle).find('input[name="customerName"]').val();
        data.consignee = $(invoiceItemContainerEle).find('input[name="consignee"]').val();
        data.notifyParty = $(invoiceItemContainerEle).find('input[name="notifyParty"]').val();
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
            url: myContextPath + "/accounts/lc/update",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/accounts/lc/list?lcNo=' + data.data, '', 'GET');

                    //                    table.ajax.reload();
                    //                    $('#modal-lc-update').modal('toggle');

                }

            }
        });

    })

    $('#table-invoice-list').on('keyup', 'input[name="amount"]', function() {
//         autoNumericSetRawValue($('#invoice-item-details').find('input[name="amount"]'));
        if ($(this).val().length != 0 && $(this).val() != 0) {
            $(this).closest('tr').find('td:first-child>input[type="checkbox"]').prop('checked', true);
        } else {
            $(this).closest('tr').find('td:first-child>input[type="checkbox"]').prop('checked', false);
        }
        var total = 0;
        $(this).closest('table').find('td>input[name="amount"]').each(function() {
            total += Number(getAutonumericValue($(this)));
        }, 0);
        $('#invoice-item-details').find('input[name="amount"]').autoNumeric('init');
        $('#table-invoice-list').find('tfoot').find('.total').autoNumeric('init').autoNumeric('set', total);
    });

})
var ifNotValid = function(val, str) {
    return typeof val === 'undefined' || val == null ? str : val;
}
function format(data) {
    var element = $('#clone-element-container').find('#invoice-item-details').clone();
    $(element).find('input[name="proformaInvoiceId"]').val(data.invoiceNo);
    $(element).find('input[name="cAddress"]').val(data.cAddress);
    $(element).find('input[name="npAddress"]').val(data.npAddress);
    $(element).find('input[name="customerName"]').val(data.firstName + ' ' + data.lastName);
    $(element).find('input[name="consignee"]').val(data.cFirstName + ' ' + data.cLastName);
    $(element).find('input[name="notifyParty"]').val(data.npFirstName + ' ' + data.npLastName);

    var row = $(element).find('table>tbody').find('tr.clone-row');
    var customerDropdownEle = $('#clone-element-container>#cutomer-dropdown').clone();
    var lctotal = 0;
    for (var i = 0; i < data.items.length; i++) {

        var isReserved = false;
        var lcAvailable = false;
        var lcCustFN, lcCustLN, lcNo = '', lcAmount = '';
        if (ifNotValid(data.items[i].lcNo, '').length != 0) {
            lcAvailable = true;
            lcNo = data.items[i].lcNo;
            lcCustFN = ifNotValid(data.items[i].lcCustFirstName, '');
            lcCustLN = ifNotValid(data.items[i].lcCustLastName, '');
            lcAmount = ifNotValid(data.items[i].lcAmount, 0);
            lctotal += lcAmount;
        } else if (data.items[i].reserve == 1 && data.customerId != data.items[i].reservedInfo.customerId) {
            isReserved = true;
        }
        var status = "";
        var statusClass = "";
        if (lcAvailable) {
            statusClass = "label-info";
            status = "LC Applied";
        } else if (isReserved) {
            statusClass = "label-warning";
            status = "Reserved";
        } else {
            statusClass = "label-success";
            status = "Available";
        }

        var rowEle = $(row).clone();
        /*numbers*/
        $(rowEle).find('.autonumber').autoNumeric('init');

        $(rowEle).find('td.s-no>input[name="stockNo[]"]').val(ifNotValid(data.items[i].stockNo, '')).prop('readonly', (lcAvailable || isReserved));
        $(rowEle).find('td.s-no>input[name="chassisNo"]').val(ifNotValid(data.items[i].chassisNo, ''))
        $(rowEle).find('td.s-no>input[name="maker"]').val(ifNotValid(data.items[i].maker, ''))
        $(rowEle).find('td.s-no>input[name="model"]').val(ifNotValid(data.items[i].model, ''))
        $(rowEle).find('td.s-no>input[name="hsCode"]').val(ifNotValid(data.items[i].hsCode, ''))
        $(rowEle).find('td.stockNo').html(ifNotValid(data.items[i].stockNo, ''));
        $(rowEle).find('td.chassisNo').html(ifNotValid(data.items[i].chassisNo, ''));
        $(rowEle).find('td.hsCode').html(ifNotValid(data.items[i].hsCode, ''));
        $(rowEle).find('td.makerModel').html(ifNotValid(data.items[i].maker, '') + ' / ' + ifNotValid(data.items[i].model, ''));
        $(rowEle).find('td.year').html(ifNotValid(data.items[i].year, ''));
        $(rowEle).find('td.customer').html((lcAvailable ? lcCustFN + ' ' + lcCustLN : (isReserved ? '' : customerDropdownEle.html())));
        if (isReserved) {
            $(rowEle).find('td.amount').html('');
        } else {
            $(rowEle).find('td.amount>input').autoNumeric('set', lcAmount).prop('readonly', (lcAvailable || isReserved));
        }

        $(rowEle).find('td.pAmount>input').autoNumeric('init').autoNumeric('set', (ifNotValid(data.items[i].total, ''))).prop('readonly', true);
        $(rowEle).find('td.status>span').addClass(statusClass).html(status);
        $(rowEle).find('td.lcNo').html(ifNotValid(data.items[i].lcNo, ''));
        $(element).find('table>tfoot').find('tr>td.total').autoNumeric('init').autoNumeric('set', lctotal)
        $(element).find('table>tfoot').find('tr>td.ptotal').autoNumeric('init').autoNumeric('set', data.total)
        $(rowEle).removeClass('hide');
        $(element).find('table>tbody').append(rowEle);
    }
    return element;
}
function customerInitSelect2(element, value) {
    $(element).select2({
        allowClear: true,
        minimumInputLength: 2,
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
function addListValidations(element) {
    $(element).find('input[name="amount"]').addClass('validate-required');
}
function removeListValidations(element) {
    $(element).find('input[name="amount"]').removeClass('validate-required');

}

function isEntryValid(element) {
    var valid = true;
    valid = elementValueNotEmpty($(element).find('input[name="amount"]'));
    return valid;
}
var elementValueNotEmpty = function(elements) {
    for (var i = 0; i < elements.length; i++) {
        if (isEmpty($(elements[i]).val())) {
            return false;
        }
    }
    return true;
}
function amountValid(element) {
    var amtValid = true;
    var amt, prfmAmt;
    autoNumericSetRawValue($(element).find('input[name="amount"]'));
    autoNumericSetRawValue($(element).find('input[name="pAmount"]'));
    amt = $(element).find('input[name="amount"]').val();
    prfmAmt = $(element).find('input[name="pAmount"]').val();
    if (Number(amt) <= Number(prfmAmt)) {
        return amtValid;
    } else {
        amtValid = false;
    }
    return amtValid;
}
