var table;
$(function() {

    var result, loanAmount, numberOfMonths, rateOfInterest, monthlyInterestRatio, top, bottom, sp, emi, full, interest, int_pge;
    var prams = ''
    $.getJSON(myContextPath + "/data/bank.json", function(data) {
        $('#bank').select2({
            data: $.map(data, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName
                };
            })
        });
    })
    $.getJSON(myContextPath + "/data/savingBank.json", function(data) {
        $('#savingsBankAccount').select2({
            data: $.map(data, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName
                };
            })
        });
    })
    //autonumeric
    $('input.autonumber').autoNumeric('init', {
        unSetOnSubmit: true
    });
    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true

    })
    $('select.select2').select2({});
    $('input[name="savingAccount"]').on('change', function() {
        let checked = $(this).is(":checked");
        if (!checked) {
            $(this).val(0);
            $('input[name="savingsAccountAmount"]').prop('disabled', true).val('');
            $('select[name="savingsBankAccount"]').prop('disabled', true).val('').trigger('change');
        } else {
            $(this).val(1);
            $('input[name="savingsAccountAmount"]').prop('disabled', false);
            $('select[name="savingsBankAccount"]').prop('disabled', false);
        }

    })
    $('#loancreateForm').on('input', '#loanamount,#loanterm,#interestrate', function() {
        var loanAmount = $("#loanamount").val();
        var numberOfMonths = $("#loanterm").val();
        var rateOfInterest = $("#interestrate").val();
        var monthlyInterestRatio = (rateOfInterest / 100) / 12;

        var top = Math.pow((1 + monthlyInterestRatio), numberOfMonths);
        var bottom = top - 1;
        var sp = top / bottom;
        var emi = ((loanAmount * monthlyInterestRatio) * sp);
        var full = numberOfMonths * emi;
        var interest = full - loanAmount;
        var int_pge = (interest / full) * 100;
        if ((!isEmpty(loanAmount)) && (!isEmpty(numberOfMonths)) && (!isEmpty(rateOfInterest)))
            $('#monthtotal').val(emi.toFixed(2));
        $('#totalpayment').val(full.toFixed(2));
        $('#totalinterest').val(interest.toFixed(2));
        $('#annualpayment').val(int_pge.toFixed(2) + " %");

    })
    //     $('button#calculate').on('click', function() {
    //         let prams = '';
    //         let rateOfInterest = getAutonumericValue($('#rateOfInterest'));
    //         let loanTerm = getAutonumericValue($('#loanTerm'));
    //         let loanAmount = getAutonumericValue($('#loanAmount'));
    //         prams += 'loanAmount=' + loanAmount + '&'
    //         prams += 'loanTerm=' + loanTerm + '&'
    //         prams += 'rateOfInterest=' + rateOfInterest

    //         $.ajax({
    //             beforeSend: function() {
    //                 $('#spinner').show()
    //             },
    //             complete: function() {
    //                 $('#spinner').hide();
    //             },
    //             type: "get",
    //             url: myContextPath + '/accounts/get/loan/calc?' + prams,
    //             async: false,
    //             success: function(data) {
    //                 result = data;
    //             }
    //         });
    //     })
    $('button#next').on('click', function() {
        if (!$('form#loancreateForm').find('input,select').valid()) {
            return;
        }
        $('.loancreation').find('.createloa').addClass('hidden')
        $('.loancreation').find('.confirmloan').removeClass('hidden')
        $('button#next').addClass('hidden')
        $('button#create').removeClass('hidden')
        $('button#previous').removeClass('hidden')

        let data = {};
        data.rateOfInterest = getAutonumericValue($('#loancreateForm').find('input#rateOfInterest'));
        data.loanTerm = getAutonumericValue($('#loancreateForm').find('input#loanTerm'));
        data.loanAmount = getAutonumericValue($('#loancreateForm').find('input#loanAmount'));
        data.monthlyEmi = getAutonumericValue($('#loancreateForm').find('input#monthlyEmi'));
        data.paymentDate = $('#loancreateForm').find('input[name="firstPaymentDate"]').val();
        data.loanType = $('#loancreateForm').find('select#loanType option:selected').val();
        data.loanId = $('#loancreateForm').find('input[name="loanId"]').val();
        loadData(data);
    })
    $('button#previous').on('click', function() {
        $('.loancreation').find('.createloa').removeClass('hidden')
        $('.loancreation').find('.confirmloan').addClass('hidden')
        $('button#next').removeClass('hidden')
        $('button#create').addClass('hidden')
        $('button#previous').addClass('hidden')
    })
    $('button#create').on('click', function() {
        autoNumericSetRawValue($('#loancreateForm').find('input.autonumber,span.autonumber'));
        var formData = getFormData($('#loancreateForm').find('input,select'));
        var objectArr = [];
        var object;
        table.rows({
            page: 'All'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = $(this.node());
            autoNumericSetRawValue($(data).find('input.autonumber,span.autonumber'));
            object = getFormData($(data).find('input'));
            object['dueDate'] = $(data).find('input#tDueDate').val();
            object['status'] = $(data).find('span.hiddenStatus').text();
            object['loanDtlId'] = $(data).find('span.loanDtlId').text();
            objectArr.push(object);
        });
        formData['loanDetails'] = objectArr;
        var loanId = $('#loancreateForm').find('input[name="loanId"]').val();
        var url;
        if(isEmpty(loanId)){
            url = "/accounts/createLoan"
        }else{
            url = "/accounts/editLoan"
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(formData),
            url: myContextPath + url,
            contentType: "application/json",
            async: false,

            success: function(data) {
                if (data.status === "success") {

                    $.redirect(myContextPath + '/accounts/loan-details', '', 'GET');
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    })

    $(document).on('keyup', 'input.calc-loan', function() {
        let rateOfInterest = getAutonumericValue($('#rateOfInterest'));
        let loanTerm = getAutonumericValue($('#loanTerm'));
        let loanAmount = getAutonumericValue($('#loanAmount'));
        if ((!isEmpty($('#loanTerm').val())) && (!isEmpty($('#loanAmount').val())) && (!isEmpty($('#rateOfInterest').val()))) {
            let monthlyEmi = loanAmount / loanTerm;
            setAutonumericValue($('#monthlyEmi'), monthlyEmi);
        }

        calcEmi(loanAmount, rateOfInterest, loanTerm);
    })
    $('form#loancreateForm').submit(function() {
        var form = $(this);
        form.find('input.autonumber').each(function(i) {
            var self = $(this);
            try {
                var v = self.autoNumeric('get');
                self.autoNumeric('destroy');
                self.val(v);
            } catch (err) {
                console.log("Not an autonumeric field: " + self.attr("name"));
            }
        });
        return true;
    });

    //for edit loan details
    var loanId = $('#loanId').val();
    if (!isEmpty(loanId)) {
        update(loanId)
    }
})

function update(loanId) {
    $.getJSON(myContextPath + '/accounts/loan/info/' + loanId, function(data) {
        var response = data.data;
        console.log(response)
        $('input[name="date"]').datepicker('setDate', response.date);
        $('select[name="bank"]').val(response.bank).trigger('change');
        $('input[name="reference"]').val(response.reference);
        $('select[name="loanType"]').val(response.loanType).trigger('change');
        $('input[name="dueDate"]').datepicker('setDate', response.dueDate);
        $('select[name="leaveDay"]').val(response.leaveDay).trigger('change');
        $('input[name="savingAccount"]').val(response.savingAccount);
        $('select[name="principalPaymentFrequency"]').val(response.principalPaymentFrequency).trigger('change');
        $('select[name="interestPaymentFrequency"]').val(response.interestPaymentFrequency).trigger('change');
        setAutonumericValue($('input[name="loanAmount"]'),response.loanAmount);
        $('input[name="firstPaymentDate"]').datepicker('setDate', response.firstPaymentDate);
        setAutonumericValue($('input[name="rateOfInterest"]'),response.rateOfInterest);
        $('input[name="loanTerm"]').val(response.loanTerm);
        setAutonumericValue($('input[name="monthlyEmi"]'),response.installmentAmount);
        $('input[name="description"]').val(response.description);
        if(response.savingAccount == 1){
            $('input[name="savingAccount"]').prop('checked',true).val(1);
            $('input[name="savingsAccountAmount"]').prop('disabled', false).val(response.savingsAccountAmount);
            $('select[name="savingsBankAccount"]').prop('disabled', false).val(response.savingsBankAccount).trigger('change');
        }else{
            $('input[name="savingAccount"]').prop('checked',false).val(0);
            $('input[name="savingsAccountAmount"]').prop('disabled', true);
            $('select[name="savingsBankAccount"]').prop('disabled', true).val('').trigger('change');
        }
    })
}

function calcEmi(loanAmount, annualInterestRate, numberOfMonths) {
    let numberOfYears = numberOfMonths / 12;
    let monthlyInterestRate = annualInterestRate / 12;
    monthlyInterestRate /= 100;
    let emi = loanAmount * monthlyInterestRate / (1 - 1 / Math.pow(1 + monthlyInterestRate, numberOfYears * 12));
    emi = ifNaN(emi, 0);
    let totalPayable = emi * numberOfYears * 12;
    setAutonumericValue($('#installmentAmount'), emi)
    setAutonumericValue($('#totalPayable'), totalPayable)
    setAutonumericValue($('#totalInterest'), totalPayable - loanAmount);
}

function loadData(params) {
    tableEle = $('#table-confirmloan')
    table = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "destroy": true,
        "ordering": false,
        "ajax": {
            url: myContextPath + "/accounts/loan-view-before-create",
            data: function(data) {
                return data = params

            },
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
            "data": "month",
            render: function(data, type, row, meta) {
                return meta.row + meta.settings._iDisplayStart + 1;
            }
        }, {
            targets: 1,
            "name": "dueDate",
            "data": "dueDate",
            "render": function(data, type, row) {
                return '<input type="text" class="form-control required datepicker" id="tDueDate" name="tDueDate" value = "' + row.dueDate + '"placeholder="DD/MM/YYYY">';

            }
        }, {
            targets: 2,
            "className": "align-center",
            "data": "principalAmt",
            "render": function(data, type, row) {

                return '<input type="text" name="principalAmt"  class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0"><span class="hidden loanDtlId">' + ifNotValid(row.loanDtlId,'') + '</span>';
            }

        }, {
            targets: 3,
            "className": "align-center",
            "data": "interestAmount",
            "render": function(data, type, row) {
                return '<input type="text" name="interestAmount"  class="form-control autonumber" value="' + Math.floor(ifNotValid(data, 0)) + '" data-a-sign="&yen; " data-m-dec="0">';
            }
        }, {
            targets: 4,
            "className": "align-center",
            "data": "amount",
            "render": function(data, type, row) {
                return '<input type="text" name="amount"  class="form-control autonumber" value="' + Math.floor(ifNotValid(data, 0)) + '" data-a-sign="&yen; " data-m-dec="0">';
            }
        }, {
            targets: 5,
            "className": "align-center",
            "data": "openingBalance",
            "render": function(data, type, row) {
                return '<input type="text" name="openingBalance"  class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0">';
            }

        }, {
            targets: 6,
            "className": "align-center",
            "data": "closingBalance",
            "render": function(data, type, row) {
                return '<input type="text" name="closingBalance"  class="form-control autonumber" value="' + ifNotValid(data, 0) + '" data-a-sign="&yen; " data-m-dec="0">';
            }

        }, {
            targets: 7,
            "data": "status",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var className = row.status == 1 ? "success" : "warning";
                return '<span class="label label-' + className + '">' + (row.status == 1 ? "Paid" : "Not Paid") + '</span><span class="hidden hiddenStatus">' + data + '</span>';
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-confirmloan span.autonumber,input.autonumber').autoNumeric('init')
             tableEle.find('input.datepicker').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true

            })
        },
    });
}
