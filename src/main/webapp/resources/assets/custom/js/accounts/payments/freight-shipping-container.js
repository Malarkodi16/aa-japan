var supplierJson, forwarderJson, forwarderDetailJson;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $('.datepicker ').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on('change', function() {//         $(this).valid();

    });
    $('#stockEntryForm').on('keydown', 'input,select,textarea,select.select2', function(e) {
        var tabindex = $(this).attr('tabindex');
        if (!isEmpty(tabindex) && tabindex == stockEntryFormLastTabIndex && e.keyCode === 9) {
            var firstElem = $('#stockEntryForm').find('select[tabindex="1"]');
            firstElem.focus();
            e.preventDefault();
        }
    })
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwarderJson = data;
        $('#container-filter-frwdr,select#forwarder').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/shipping/vessalsAndVoyageNo.json", function(data) {
        $('#container-filter-vessel').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data.data, function(item) {
                return {
                    id: item.scheduleId,
                    text: item.shipName + ' / ' + item.voyageNo + ' [' + item.shippingCompanyName + ']'
                };
            })
        });
    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        $('#container-filter-orginCountry,#container-filter-destinationCountry').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        }).val('').trigger('change');

    })
    $('#container-filter-orginPort,#container-filter-destinationPort').select2({
        allowClear: true,
        width: '100%'
    })
    $('#modal-invoice select[name="description"]').select2({
        allowClear: true
    }).val('').trigger('change');
    $('select#container-filter-blNo').select2({
        allowClear: true,
        width: '100%',
        tags: true
    })

    $('#container-filter-orginCountry').change(function() {
        setPort(this, '#container-filter-orginPort')
    })
    $('#container-filter-destinationCountry').change(function() {
        setPort(this, '#container-filter-destinationPort')
    })
    $('#container-filter-search').on('click', function() {
        table_shipping_container.ajax.reload();
    })

    $('div#modal-invoice input.autonumber').autoNumeric('init')
    var table_shipping_container_element = $('table#tableShippingContainer');
    var table_shipping_container = table_shipping_container_element.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "ajax": {
            "url": myContextPath + "/freight/shippingRequestContainer",
            "type": "get",
            "data": function(data) {
                data.forwarder = $('select#container-filter-frwdr').val();
                data.scheduleIds = $('select#container-filter-vessel').val();
                data.orginCountry = $('select#container-filter-orginCountry').val();
                data.orginPort = $('select#container-filter-orginPort').val();
                data.destinationCountry = $('select#container-filter-destinationCountry').val();
                data.destinationPort = $('select#container-filter-destinationPort').val();
                data.blNo = $('select#container-filter-blNo').val();

            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            visible: false,
            "data": "id",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" id="check-box-select" type="checkbox">';

                }
                return data;
            }
        }, {
            targets: 1,
            orderable: true,
            className: 'details-control',
            "data": "chassisNo",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "maker"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "model"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "destinationCountry"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "salesPerson"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "m3"
        }, {
            targets: 7,
            "className": "details-control",
            "data": "contName"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "amount",
            "render": function(data, type, row) {
              return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>'
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var api = this.api(), data;
            // Total over all pages
            totalM3 = api.column(6).data().reduce(function(a, b) {
                return Number(a) + Number(b);
            }, 0);
            // Total over all pages
            totalAmount = api.column(8).data().reduce(function(a, b) {
                return Number(a) + Number(b);
            }, 0);

            // Update footer
            $(api.column(6).footer()).html(totalM3);
            setAutonumericValue($(api.column(8).footer()).find('span'), totalAmount);

        },
        "drawCallback": function(settings, json) {
            $('table#tableShippingContainer span.autonumber').autoNumeric('init')

        }

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    };

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table_shipping_container.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table_shipping_container.page.len($(this).val()).draw();
    });
    let modalCreateInvoice = $('#modal-create-invoice');
    let modalInvoice = $('#modal-invoice');
    modalCreateInvoice.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (table_shipping_container.rows().count() <= 0) {
            alert('No stock found.!')
            return false;
        }
        let items = modalInvoice.find('table>tbody>tr').not(':first').toArray();
        if (items.length <= 0) {
            alert('No invoice found!');
            return false;
        }
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#btnCreateInvoice', function() {
        let items = modalInvoice.find('table>tbody>tr').not(':first').toArray();
        let invoiceItems = items.map(item=>{
            let quantity = $(item).find('td>span.qty').html();
            let description = $(item).find('td>span.description').html();
            let usd = getAutonumericValue($(item).find('td>span.usd'));
            let zar = getAutonumericValue($(item).find('td>span.zar'));
            let unitPrice = getAutonumericValue($(item).find('td>span.unitprice'));
            let amount = getAutonumericValue($(item).find('td>span.amount'));
            let data = {};
            data["quantity"] = quantity;
            data["description"] = description;
            data["usd"] = usd;
            data["zar"] = zar;
            data["unitPrice"] = unitPrice;
            data["amount"] = amount;
            return data;
        }
        )
        let listShippingRequestData = table_shipping_container.rows().data().toArray().map(tr=>{
            let data = {};
            data["shipmentRequestId"] = tr.shipmentRequestId;
            data["amount"] = tr.amount;
            return data;
        }
        )
        let data = {};
        data["invoiceItems"] = invoiceItems;
        data["shipmentRequest"] = listShippingRequestData;
        data["invoiceDate"] = modalCreateInvoice.find('input[name="date"]').val();
        data["forwarder"] = modalCreateInvoice.find('select#forwarder').val();
        data["dueDate"] = modalCreateInvoice.find('input[name="dueDate"]').val();
        data["usdExchangeRate"] = getAutonumericValue(modalInvoice.find('input[name="currencyRateUSD"]'));
        data["zarExchangeRate"] = getAutonumericValue(modalInvoice.find('input[name="currencyRateZAR"]'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            contentType: "application/json",
            async: true,
            url: myContextPath + '/freight/shipping/containerInvoice/create',
            success: function(data) {
                if (data.status == 'success') {
                    table_shipping_container.ajax.reload();
                    modalCreateInvoice.modal('toggle');
                }
            }
        })

    })
    // $(modalInvoice).find('input,select :visible').each(function (i) { $(this).attr('tabindex', i + 1); });
    //modal invoice
    modalInvoice.on('show.bs.modal', function(e) {
        if (table_shipping_container.rows().count() <= 0) {
            alert($.i18n.prop('common.alert.no.stock'))
            return false;
        }
    }).on('change', 'select[name="description"]', function() {
        let value = $(this).find('option:selected').val();
        let currency = $(this).find('option:selected').attr('data-currency');
        if (isEmpty(currency) || isEmpty(value)) {
            modalInvoice.find('input[name="usdAmount"],input[name="zarAmount"],input[name="amount"]').val('').prop('readonly', true);
        } else {
            let elementUSD = modalInvoice.find('input[name = "currencyRateUSD"]')
            let elementZAR = modalInvoice.find('input[name = "currencyRateZAR"]')
            if (currency == 'usd') {
                let usdRate = getAutonumericValue(elementUSD);
                if (!isEmpty(usdRate)) {
                    modalInvoice.find('input[name="usdAmount"]').prop('readonly', false)
                    modalInvoice.find('input[name="zarAmount"]').prop('readonly', true).val('').trigger('change');
                } else {
                    alert('Please enter USD currency rate!')
                    $(this).val('').trigger('change');
                    return false
                }
            } else if (currency == 'zar') {
                let zarRate = getAutonumericValue(elementZAR);
                if (!isEmpty(zarRate)) {
                    modalInvoice.find('input[name="zarAmount"]').prop('readonly', false)
                    modalInvoice.find('input[name="usdAmount"]').prop('readonly', false).prop('readonly', true).val('').trigger('change');
                } else {
                    alert('Please enter ZAR currency rate!')
                    $(this).val('').trigger('change');
                    return false
                }

            }
        }
    }).on('keyup', 'input[name="quantity"]', function() {
        let usdRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateUSD"]'))
        let zarRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateZAR"]'))
        if (!(!isEmpty(usdRate) && usdRate > 0) || !(!isEmpty(zarRate) && zarRate > 0)) {
            alert("Please enter USD and ZAR currency rate.! ")
            $(this).val('');
            return false;
        }
        let val = getAutonumericValue(this);
        if (!isEmpty(val) && val > 0) {
            modalInvoice.find('select[name="description"]').removeClass('select2-select readonly');
        } else {
            modalInvoice.find('select[name="description"]').addClass('select2-select readonly');
            modalInvoice.find('select[name="description"]').val('').trigger('change');
        }
    }).on('keyup', 'input[name="usdAmount"]', function() {
        let currency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        if (currency == "usd") {
            doCalc()
        }
    }).on('keyup', 'input[name="zarAmount"]', function() {
        let currency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        if (currency == "zar") {
            doCalc()
        }
    }).on('keyup', 'input[name="quantity"]', function() {
        //         let currency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        //         if (currency == "zar") {
        doCalc()
        //         }
    }).on('keyup', 'input[name="unitPrice"]', function() {
        //         let currency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        //         if (currency == "zar") {
        doUnitPriceCalc()
        //         }
    }).on('click', 'button[name="btn-add"]', function() {
        let usdAmount = getAutonumericValue(modalInvoice.find('input[name="usdAmount"]'));
        let zarAmount = getAutonumericValue(modalInvoice.find('input[name="zarAmount"]'));
        //         if (isEmpty(usdAmount) && isEmpty(zarAmount)) {
        //             alert("Please enter valid data.")
        //             return false;
        //         }
        //         doCalc();
        modalInvoice.find('input[name="currencyRateUSD"],input[name="currencyRateZAR"]').prop('readonly', true)
        let tbody = modalInvoice.find('table>tbody');
        let template = tbody.find('tr:first-child');
        let qty = modalInvoice.find('input[name="quantity"]').val();
        let description = modalInvoice.find('select[name="description"]').val();
        let currency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');

        let unitPrice = getAutonumericValue(modalInvoice.find('input[name="unitPrice"]'));
        let amount = getAutonumericValue(modalInvoice.find('input[name="amount"]'));
        let row = template.clone();
        row.find('td>span.qty').html(qty);
        row.find('td>span.description').html(description);
        if (currency == 'usd') {
            setAutonumericValue(row.find('td>span.usd'), usdAmount)
        } else if (currency == 'zar') {
            setAutonumericValue(row.find('td>span.zar'), zarAmount)
        }
        setAutonumericValue(row.find('td>span.unitprice'), unitPrice)
        setAutonumericValue(row.find('td>span.amount'), amount)
        row.removeClass('hidden');
        modalInvoice.find('input[name="quantity"]').val('').trigger('keyup');
        tbody.append(row)
        invoiceTotalSum();

    }).on('click', 'tbody button[name="btn-delete"]', function() {
        $(this).closest('tr').remove();
        invoiceTotalSum();
    }).on('click', 'button#btn-save', function() {
        let items = modalInvoice.find('table>tbody>tr').not(':first').toArray();
        if (items.length <= 0) {
            alert('No payment found!');
            return false;
        }
        let tableData = table_shipping_container.data().toArray();
        let totalM3 = tableData.reduce(function(a, b) {
            return Number(a) + Number(b.m3);
        }, 0)
        let invoiceTotal = getAutonumericValue(modalInvoice.find("#invoiceTotal"));

        let con = 'A';
        let con1 = 'A';
        var conM3Arr = [];
        let i = 0;
        conM3Arr[0] = 0;

        for (let k = 0; k < tableData.length; k++) {

            con1 = tableData[k].contName;
            if (con == con1) {
                conM3Arr[i] += tableData[k].m3;
            } else {
                i++;
                conM3Arr[i] = 0;
                con = tableData[k].contName;

                conM3Arr[i] += tableData[k].m3;
            }
        }

        let perM3 = Number(invoiceTotal) / ((i + 1));
        let j = 0;
        for (let h = 0; h < conM3Arr.length; h++) {
            tableData = tableData.map(data=>{

                if (String.fromCharCode(h + 65) == data.contName)
                    data.amount = ifNotValid(ifNotValid(data.m3, 0) * (perM3 / conM3Arr[h]),0);
                    data.amount=ifNaN(data.amount,0);
                return data;
            }

            )
        }
        table_shipping_container.rows().invalidate(tableData).draw(false);
        modalInvoice.modal('toggle');
    })

    var invoiceTotalSum = function() {
        let items = modalInvoice.find('table>tbody>tr').not(':first').toArray();        
        let totalUsd = items.reduce(function(a, b) {
            return Number(a) + Number(getAutonumericValue($(b).find('td>span.usd')));
        }, 0)
        let total = items.reduce(function(a, b) {
            return Number(a) + Number(getAutonumericValue($(b).find('td>span.amount')));
        }, 0)
        let totalUsdElem = modalInvoice.find('table>tfoot>tr:first-child  td>span.totalUsd');
        let totalElem = modalInvoice.find('table>tfoot>tr:first-child  td>span.total');
        setAutonumericValue(totalUsdElem, totalUsd);
        setAutonumericValue(totalElem, total);
    }
    function doCalc() {
        let descriptionElementCurrency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        let amount, currencyRate;
        if (descriptionElementCurrency == 'usd') {
            amount = getAutonumericValue(modalInvoice.find('input[name="usdAmount"]'));
            currencyRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateUSD"]'));
        } else if (descriptionElementCurrency == 'zar') {
            amount = getAutonumericValue(modalInvoice.find('input[name="zarAmount"]'));
            currencyRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateZAR"]'));
        }

        let qty = getAutonumericValue(modalInvoice.find('input[name="quantity"]'));
        let total = Number(amount) * Number(currencyRate);
        let unitPrice = Number(total) / Number(qty);
        setAutonumericValue(modalInvoice.find('input[name="unitPrice"]'), isNaN(unitPrice) ? 0 : unitPrice);
        setAutonumericValue(modalInvoice.find('input[name="amount"]'), isNaN(unitPrice) ? 0 : total);
    }
    function doUnitPriceCalc() {
        //         let descriptionElementCurrency = modalInvoice.find('select[name="description"] :selected').attr('data-currency');
        //         let amount, currencyRate;
        //         if (descriptionElementCurrency == 'usd') {
        //             amount = getAutonumericValue(modalInvoice.find('input[name="usdAmount"]'));
        //             currencyRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateUSD"]'));
        //         } else if (descriptionElementCurrency == 'zar') {
        //             amount = getAutonumericValue(modalInvoice.find('input[name="zarAmount"]'));
        //             currencyRate = getAutonumericValue(modalInvoice.find('input[name="currencyRateZAR"]'));
        //         }

        let qty = getAutonumericValue(modalInvoice.find('input[name="quantity"]'));
        let unitPrice = Number(getAutonumericValue(modalInvoice.find('input[name="unitPrice"]')));
        let total = Number(qty) * Number(unitPrice);
        setAutonumericValue(modalInvoice.find('input[name="unitPrice"]'), unitPrice);
        setAutonumericValue(modalInvoice.find('input[name="amount"]'), total);
    }
})

function setPort(countryElement, portElement) {
    let val = $(countryElement).select2('data');
    let portList = [];
    if (!isEmpty(val) && val.length > 0) {
        for (let i = 0; i < val.length; i++) {
            let data = val[i]['data'];
            portList = portList.concat(data['port']);
        }
    }
    var elements = $(portElement).empty();
    elements.select2({
        allowClear: true,
        width: '100%',
        data: $.map(portList, function(item) {
            return {
                id: item,
                text: item
            };
        })
    }).val('').trigger('change');
}
function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}
