var consigneeId = 1, countryJson;
$(function() {
    $(".phone").inputmask({
        mask: ""
    });
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });

    $('input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    $(document).on('ifChecked', '#sameAsCustomer', function() {
        let companys = $('input[name="companyName"]').val();
        let address = $('input[name="address"]').val();
        $('#consigneeForm').find('input[name="cFirstName"]').val(!isEmpty(companys) ? companys : "");
        $('#consigneeForm').find('textarea[name="cAddress"]').val(!isEmpty(address) ? address : "");
    }).on('ifUnchecked', '#sameAsCustomer', function() {
        $('#consigneeForm').find('input:visible,textarea:visible').val('').attr('readonly', false);
    })
    $(document).on('ifChecked', '#sameAsConsignee', function() {
        let companyc = $('input[name="companyName"]').val();
        let address = $('input[name="address"]').val();
        $('#nofityPartyForm').find('input[name="npFirstName"]').val(!isEmpty(companyc) ? companyc : "");
        $('#nofityPartyForm').find('textarea[name="npAddress"]').val(!isEmpty(address) ? address : "");
    }).on('ifUnchecked', '#sameAsConsignee', function() {
        $('#nofityPartyForm').find('input:visible,textarea:visible').val('').attr('readonly', false);
    })
    $('.creditBalance').autoNumeric('init');

    //     $('#customerForm').on('focus', 'input[name="mobileNo"]', function(e) {
    //         $(this).on('wheel', function(e) {
    //             e.preventDefault();
    //         });
    //     })

    //     $('input[name="mobileNo"]').inputmask({
    //         mask: "(99) 9999-999-999"
    //     });
    //     $('input[name="cMobileNo"],input[name="npMobileNo"]').inputmask({
    //         mask: "(99) 9999-999-999"
    //     });

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        $(".con_country").select2({
            allowClear: true,
            width: '100%',
            data: $.map(countryJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger("change");
    });

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        $(".currencyType").select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency
                };
            })
        }).val('').trigger("change");
    });

    $.getJSON(myContextPath + "/data/foreignBanks.json", function(data) {
        $(".bank").select2({
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

    $('#lcCustomer').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });
    $('#checkCreditLimit').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });

    $('#flag2').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });
    $('#flag').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });

    updateFields();

    $(".save-customer").on('click', function() {
        if (!$("#customerForm").valid()) {
            return;
        }
        var consigneeBlock = $("#customerForm").find('.consignee-block').find('input[name="consigneeData"]')
        if (isEmpty(consigneeBlock.val())) {
            alert($.i18n.prop('alert.customer.create.atleast.one.consignee'))
            return;
        }
        if (!confirm('Do you want to save?')) {
            return false;
        }

        var consigneeArry = [];
        var data = {}
        var json = {};
        var deleteFlagArry = [];
        $('#consignee-container').find('.consignee-block').each(function() {
            json = $(this).find('input[name="consigneeData"]').val();
            json = JSON.parse(json);
            consigneeArry.push(json);
            deleteFlagArry.push(json.deleteFlag)
        })
        var deleteBoolean = false;
        var count = 0;
        for (var i = 0; i < deleteFlagArry.length; i++) {
            if (deleteFlagArry[i] == 1) {
                count++;
            }
            if (deleteFlagArry.length == count) {
                deleteBoolean = false;
                alert($.i18n.prop('alert.customer.create.atleast.one.consignee'))
                return

            } else {
                deleteBoolean = true;
            }

        }
        autoNumericSetRawValue($('#customerForm').find('input[name="creditBalance"]'))
        data = getFormData($(".consignee-data"));
        data['lcCustomer'] = $('#lcCustomer').is(":checked") ? true : false;
        data['flag'] = $('input[name="flag"]:checked').val();
        data['checkCreditLimit'] = $('#checkCreditLimit').is(":checked") ? 1 : 0;

        let npFlag = consigneeArry.filter(function(index) {
            return !isEmpty(index.npFirstName);
        });
        if (npFlag.length == 0) {
            let data = {};
            data['npFirstName'] = $.i18n.prop('same.as.consignee');
            consigneeArry.push(data);
        }
        data['consigneeNotifyparties'] = consigneeArry;
        var button = $(this).data().value;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/customer/save-customer",
            contentType: "application/json",
            success: function(status) {
                if (button == 0) {
                    $.redirect(myContextPath + '/customer/list', '', 'GET');
                } else {
                    $.redirect(myContextPath + '/customer/create', '', 'GET');
                }

            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Email Already Exist').fadeIn().delay(3000).fadeOut();
            }
        });
    });

    //     $("#save_continue_customer").on('click', function(e) {
    //         saveorSaveContinue(e);
    //     });

    let consigneeEle = $('#add-consignee');
    $('#add_consignee').on('click', function() {
        if (!$("#consigneeForm").valid()) {
            return;
        }
        var object;
        var consigneeModal = $('#consigneeModal')
        object = getFormData(consigneeModal.find('input,textarea'));
        appendConsignee(object);
        $(consigneeEle).modal('toggle');
        $(consigneeEle).find('#update-id').val('');
        consigneeModal.find("input,textarea").val('');
    });
    $('#close_consignee').on('click', function() {
        var consignee = $('#consigneeModal');
        $(consigneeEle).modal('toggle');
        $(consigneeEle).find('#update-id').val('');
        consignee.find("input,textarea").val('');
    })

    let notifyPartyEle = $('#add-notify-party')
    $('#add_notify_party').on('click', function() {
        if (!$("#nofityPartyForm").valid()) {
            return;
        }
        var object;
        var notifyPartyModal = $('#notifyPartyModal');
        object = getFormData(notifyPartyModal.find('input,textarea'));
        appendNotifyParty(object);
        $(notifyPartyEle).modal('toggle');
        $(notifyPartyEle).find('#update-id').val('');
        notifyPartyModal.find("input,textarea").val('');
    });
    $('#close_notify_party').on('click', function() {
        var notifyParty = $('#notifyPartyModal');
        $(notifyPartyEle).modal('toggle');
        $(notifyPartyEle).find('#update-id').val('');
        notifyParty.find("input,textarea").val('');
    })

    $('#add-consignee').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var triggerEle = $(e.relatedTarget);
        var flag;
        if ($(triggerEle).attr('data-action') == 'add') {
            flag = 0;
            $(this).find('#add_consignee').text('Add');
        } else {
            flag = 1;
            var updateId = $(triggerEle).closest('.consignee-block').attr('data-update')
            $(this).find('#update-id').val(updateId);

            $(this).find('#add_consignee').text('Update');
        }
        if (flag == 1) {
            var data = $(triggerEle).closest('.consignee-block').find('input[name="consigneeData"]').val();
            var edit = JSON.parse(data)
            var consignee = $('#consigneeModal')
            $(this).find('#id').val(edit.id);
            $(this).find('#deleteFlag').val(edit.deleteFlag);
            // Consignee Part
            $(consignee).find('input[name="cFirstName"]').val(edit.cFirstName);
            $(consignee).find('input[name="cLastName"]').val(edit.cLastName);
            $(consignee).find('input[name="cEmail"]').val(edit.cEmail);
            $(consignee).find('input[name="cMobileNo"]').val(edit.cMobileNo);
            $(consignee).find('textarea[name="cAddress"]').val(edit.cAddress);

        }

        if (isEmpty($('#custId').val())) {
            $('#add-consignee').find('#sameAsCustomerDiv').removeClass('hidden')
            $('#add-notify-party').find('#sameAsConsigneeDiv').removeClass('hidden')
            
        } else {
            $('#add-consignee').find('#sameAsCustomerDiv').addClass('hidden')
            $('#add-notify-party').find('#sameAsConsigneeDiv').addClass('hidden')
        }

    });

    $('#add-notify-party').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var triggerEle = $(e.relatedTarget);
        var flag;
        if ($(triggerEle).attr('data-action') == 'add') {
            flag = 0;
            $(this).find('#add_notify_party').text('Add');
        } else {
            flag = 1;
            var updateId = $(triggerEle).closest('.consignee-block').attr('data-update')
            $(this).find('#update-id').val(updateId);

            $(this).find('#add_notify_party').text('Update');
        }
        if (flag == 1) {
            var data = $(triggerEle).closest('.consignee-block').find('input[name="consigneeData"]').val();
            var edit = JSON.parse(data)
            var consignee = $('#notifyPartyModal')
            $(this).find('#id').val(edit.id);
            $(this).find('#deleteFlag').val(edit.deleteFlag);
            // Notify party Part
            $(consignee).find('input[name="npFirstName"]').val(edit.npFirstName);
            $(consignee).find('input[name="npLastName"]').val(edit.npLastName);
            $(consignee).find('input[name="npEmail"]').val(edit.npEmail);
            $(consignee).find('input[name="npMobileNo"]').val(edit.npMobileNo);
            $(consignee).find('textarea[name="npAddress"]').val(edit.npAddress);
        }

    });

    $('#consignee-container').on('click', '.delete-consignee', function() {
        if (!confirm($.i18n.prop('confirm.delete.consignee'))) {
            return;
        }
        var ele = $(this).closest('.consignee-block');
        var data = ele.find('input[name="consigneeData"]').val();
        data = JSON.parse(data);
        data.deleteFlag = 1;
        ele.find('input[name="consigneeData"]').val(JSON.stringify(data))
        ele.addClass('hidden').removeClass('active');
    });
    $('#consignee-container').on('click', '.delete-notify-party', function() {
        if (!confirm($.i18n.prop('confirm.delete.consignee'))) {
            return;
        }
        var ele = $(this).closest('.consignee-block');
        var data = ele.find('input[name="consigneeData"]').val();
        data = JSON.parse(data);
        data.deleteFlag = 1;
        ele.find('input[name="consigneeData"]').val(JSON.stringify(data))
        ele.addClass('hidden').removeClass('active');
    });
    $('#customerForm').on('change', '.con_country', function(event) {
        var selectedVal = $(this).find('option:selected').val();
        var portEle = $('.port');
        var yardEle = $('.yard');
        if (!isEmpty(selectedVal) && selectedVal.length > 0) {
            $(portEle).empty();
            var tmpPort;
            if (selectedVal.toUpperCase() == 'KENYA') {
                $('#yardFields').css('display', '')
            } else {
                $('#yardFields').css('display', 'none')
            }
            var data = filterOneFromListByKeyAndValue(countryJson, "country", selectedVal);
            $(portEle).select2({
                allowClear: true,
                width: '100%',
                placeholder: 'Select Port',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            })
            $(yardEle).select2({
                allowClear: true,
                width: '100%',
                placeholder: 'Select Yard',
                data: $.map(data.yardDetails, function(item) {
                    return {
                        id: item.id,
                        text: item.yardName
                    };
                })
            })
        } else {
            $('#yardFields').css('display', 'none')
        }
        $(portEle).val('').trigger('change');

    });

    /* $('#customerForm').on('change', '.con_country', function(event) {
	    var country = $(this).val();
	    var portElement = $(this).closest('.row').find('.port');
	    portElement.empty();
	    if (isEmpty(country)) {
	        portElement.empty();
	        return;
	    }
	    $.ajax({
	        beforeSend: function() {
	            $('#spinner').show()
	        },
	        complete: function() {
	            $('#spinner').hide();
	        },
	        type: "GET",
	        async: false,
	        url: myContextPath + "/country/find-port/" + country,
	        success: function(data) {
	            portElement.empty();
	            portElement.select2({
	                allowClear: true,
	                placeholder: 'Select Port',
	                data: $.map(data.port, function(item) {
	                    return {
	                        id: item,
	                        text: item
	                    };
	                })
	            });
	            portElement.val('').trigger("change");
	        },
	        error: function(e) {
	            console.log("ERROR: ", e);
	        }
	    });

	})*/

    $('.port').select2({
        allowClear: true,
        placeholder: 'Select Port'
    });
    $('.yard').select2({
        allowClear: true,
        placeholder: 'Select yard'
    });
    $('.currencyType').select2({
        allowClear: true,
        placeholder: 'Select Currency Type'
    });
    $('.paymentType').select2({
        allowClear: true,
        placeholder: 'Select Payment Type'
    });
    //currencyType paymentType
});
function updateFields() {
    var customerId = $('#custId').val();
    if (isEmpty(customerId)) {
        return;
    }
    var consigneeArry = [];
    var data = {}
    var json = {};
    var consigneeJson = {};

    $.getJSON(myContextPath + "/customer/data/" + customerId, function(data) {
        var customer = $('#customerForm');
        var customerCountryEle = $('#customerCountry');
        if (data.data.lcCustomer == true) {
            $(customer).find('input[name="lcCustomer"].minimal').iCheck('check');
        }

        if (data.data.checkCreditLimit == 1) {
            $(customer).find('input[name="checkCreditLimit"]').iCheck('check');
        }
        $(customer).find('input[name="id"]').val(data.data.id);
        $(customer).find('input[name="code"]').val(data.data.code);
        $(customer).find('input[name="firstName"]').val(data.data.firstName);
        $(customer).find('input[name="lastName"]').val(data.data.lastName);
        $(customer).find('input[name="nickName"]').val(data.data.nickName);
        $(customer).find('input[name="mobileNo"]').val(data.data.mobileNo);
        $(customer).find('input[name="email"]').val(data.data.email);
        $(customer).find('input[name="companyName"]').val(data.data.companyName);
        $(customer).find('input[name="skypeId"]').val(data.data.skypeId);
        $(customer).find('textarea[name="comments"]').val(data.data.comments);
        $(customer).find('input[name="address"]').val(data.data.address);
        $(customer).find('input[name="city"]').val(data.data.city);
        $(customer).find('input[name="creditBalance"]').val(data.data.creditBalance);
        $(customer).find('select[name="country"]').val(data.data.country).trigger('change');
        $(customer).find('select[name="port"]').val(data.data.port).trigger("change");
        $(customer).find('select[name="yard"]').val(data.data.yard).trigger("change");
        $(customer).find('select[name="currencyType"]').val(data.data.currencyType).trigger("change");
        $(customer).find('select[name="paymentType"]').val(data.data.paymentType).trigger("change");
        $(customer).find('select[name="bank"]').val(data.data.bank).trigger("change");
        $(customer).find('input[name="accountNo"]').val(data.data.accountNo);
        if (data.data.flag == 0) {
            $(customer).find('input[id="flag2"]').iCheck('check');
        } else if (data.data.flag == 1) {
            $(customer).find('input[id="flag"]').iCheck('check');
        }

        var consigneeData = data.data.consigneeNotifyparties;

        for (var i = 0; i < consigneeData.length; i++) {
            if (consigneeData[i].deleteFlag == 0) {
                if (isEmpty(consigneeData[i].npFirstName)) {
                    appendConsignee(consigneeData[i]);
                } else {
                    appendNotifyParty(consigneeData[i])
                }
            }

        }
    });
    $('.search-block').find('input[name="code"]').val('');
}
function appendConsignee(data) {
    customer = JSON.stringify(data)
    var consigneeEle = $('#consignee-clone-container').find('.consignee-block').clone()
    $(consigneeEle).find('input[name="consigneeData"]').val(customer)
    $(consigneeEle).find('#fName').html(data.cFirstName);
    $(consigneeEle).find('#address').html(data.cAddress);
    $(consigneeEle).find('#lName').html(data.cLastName);
    $(consigneeEle).find('#phoneNo').html(data.cMobileNo);
    $(consigneeEle).attr('data-update', consigneeId++);
    $(consigneeEle).find('.clearfix').find('p.consignee').removeClass('hidden')
    $(consigneeEle).find('.clearfix').find('p.notifyParty').remove()

    var updateId = $('#add-consignee').find('#update-id').val();

    if (updateId.length != 0) {
        var updateEle = $('#consignee-container').find('div[data-name="consigneeBlock"][data-update="' + updateId + '"]')
        $(updateEle).find('#view-consignee').remove();
        var replaceEle = $(consigneeEle).find('#view-consignee');
        $(updateEle).append(replaceEle);
    } else {
        $('#consignee-container').append(consigneeEle);
    }
}
function appendNotifyParty(data) {
    customer = JSON.stringify(data)
    var consigneeEle = $('#consignee-clone-container').find('.consignee-block').clone()
    $(consigneeEle).find('input[name="consigneeData"]').val(customer)
    $(consigneeEle).find('#fName').html(data.npFirstName);
    $(consigneeEle).find('#address').html(data.npAddress);
    $(consigneeEle).find('#lName').html(data.npLastName);
    $(consigneeEle).find('#phoneNo').html(data.npMobileNo);
    $(consigneeEle).attr('data-update', consigneeId++);
    $(consigneeEle).find('.clearfix').find('p.notifyParty').removeClass('hidden')
    $(consigneeEle).find('.clearfix').find('p.consignee').remove()

    var updateId = $('#add-notify-party').find('#update-id').val();

    if (updateId.length != 0) {
        var updateEle = $('#consignee-container').find('div[data-name="consigneeBlock"][data-update="' + updateId + '"]')
        $(updateEle).find('#view-consignee').remove();
        var replaceEle = $(consigneeEle).find('#view-consignee');
        $(updateEle).append(replaceEle);
    } else {
        $('#consignee-container').append(consigneeEle);
    }
}
