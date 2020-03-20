//common event
$(function() {

    $(document).on('blur', 'input[type="text"]:not(.skip-toUppercase),textarea:not(.skip-toUppercase)', function() {
        $(this).val($(this).val().trim().toUpperCase());
    });
    $(document).on('focus', 'input,select,textarea,span.select2', function() {
        $(this).addClass('highlight-outline');
    }).on('blur', 'input,select,textarea,span.select2', function() {
        $(this).removeClass('highlight-outline');
    })
    //auto-completeoff in jsp pages
    $('input').attr('autocomplete', 'off');
    //messages.properties
    $.i18n.properties({
        name: 'messages',
        path: myContextPath + '/resources/i18n',
        mode: 'both',
        language: 'en'
    });

    if ($('#alert-block').is(":visible") == true)
        $('#alert-block').fadeTo(5000, 500).slideUp(500, function() {
            $("#alert-block").slideUp(500);
        });
    // Get the modal
    var modalImage = $('#myModalImagePreview');
    $(document).on('click', '.img-responsive', function() {
        modalImage.css('display', 'block');
        modalImage.find('img.modal-content-img').attr('src', this.src);
        // 	$(this).src = this.src;
    })

    // Get the <span> element that closes the modal
    $(document).on('click', '#myModalImagePreview', function() {
        modalImage.css('display', 'none');
    })

})
function executeWithDelay(callback, timeInMilliseconds) {
    $('div#spinner').show();
    setTimeout(callback, timeInMilliseconds);

}
function isValidFileSelected(element) {
    var fileExtension = ['jpeg', 'jpg', 'png', 'gif', 'bmp', 'pdf', 'xlsx', 'xls', 'csv'];
    if ($.inArray($(element).val().split('.').pop().toLowerCase(), fileExtension) == -1) {
        alert("Only formats are allowed : " + fileExtension.join(', '));
        return false
    }
    let files = $(element).prop('files');
    for (let i = 0; i < files.length; i++) {
        if (files[i].size > 10000000) {
            alert("Files size should not exceed 10 MB");
            return false;
        }
    }
    return true;
}
function generateDropdown(data, valKey, textKey, selectedVal) {
    var html = '<option value=""></option>'
    var tmpVal;
    $.each(data, function(index, item) {
        tmpVal = (valKey != '' ? item[valKey] : item)
        html += '<option value="' + tmpVal + '" ' + (selectedVal == tmpVal ? 'selected' : '') + '>' + (textKey != '' ? item[textKey] : item) + '</option>'
    });
    return html;
}
function getFormData(element) {
    var unindexed_array = element.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i) {

        indexed_array[n['name']] = n['value'];

    });
    return indexed_array;
}

function genarateOptionsHtml(list, selectedVal, val, text) {
    var html = '<option value=""></option>';
    $.each(list, function(index, value) {
        html += '<option value="' + (val.length != 0 ? value[val] : value) + '" ' + ((val.length != 0 ? value[text] : value) == selectedVal ? "selected" : "") + '>' + (val.length != 0 ? value[text] : value) + '</option>';
    });
    return html;
}

function getListOfList(list, selectedVal, matchKey, getKey) {
    var val = [];
    $.each(list, function(index, value) {
        if (value[matchKey] == selectedVal) {
            val = value[getKey]
            return false;
        }
    });
    return val;
}
function autoNumericSetRawValue(element) {
    $(element).each(function(i) {
        var self = $(this);
        try {
            var v = self.autoNumeric('get');
            self.autoNumeric('destroy');
            self.val(v);
        } catch (err) {
            console.log("Not an autonumeric field: " + self.attr("name"));
        }
    });
}
function resetElementInput(element) {
    $(element).find("input,textarea").val('').end().find("input[type=checkbox], input[type=radio]").prop("checked", "").end();

}
function getuuid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}

function isExistNested(obj /* , level1, level2, ... levelN */
) {
    var args = Array.prototype.slice.call(arguments, 1);

    for (var i = 0; i < args.length; i++) {
        if (!obj || !obj.hasOwnProperty(args[i])) {
            return false;
        }
        obj = obj[args[i]];
    }
    return true;
}
function ifNotValid(val, str) {
    return typeof val === 'undefined' || val == null ? str : val;
}
function ifNaN(val, returnVal) {
    return isNaN(val) ? returnVal : val;
}
function filterOneFromListByKeyAndValue(list, key, value) {
    var data = $.grep(list, function(element, index) {

        return element[key] == value;
    });

    return data.length == 1 ? data[0] : null;

}
function isEmpty(val) {
    if (val instanceof Object) {
        return Object.keys(val).length == 0 ? true : false;
    }
    return ifNotValid(val, '').length == 0 ? true : false;
}
function setShippingDashboardStatus() {

    $.ajax({
        //         beforeSend: function() {
        //             $('#stock-status-wrapper div.overlay').show()
        //         },
        //         complete: function() {
        //             $('#stock-status-wrapper div.overlay').hide();
        //         },
        type: "get",
        url: myContextPath + "/data/shipping-dashboard/status-count",
        contentType: "application/json",
        success: function(data) {
            data = data.data;
            $('#purchased-count').html(data.purchased);
            $('#purchase-confirmed-count').html(data.purchasedConfirm);
            $('#transportation-count').html(data.inTransit);
            $('#inspection-count').html(data.inspection);
            $('#shipping-arrangement-count').html(data.shippingRoro + " / " + data.shippingContainer);
            $('#shipping-status-count').html(data.shippingStatus);

        }
    });

}
function setDocumentDashboardStatus(data) {
    $('#notReceived-count').html(data.notReceived);
    $('#received-count').html(data.received);
    $('#certificate-count').html(data.exportCertificate);
    $('#nameTransfer-count').html(data.nameTransfer);
    $('#domestic-count').html(data.domestic);

}
function setDocumentDashboardCount(data) {
    $('#draft-bl').html(data.draftBL);
    $('#original-bl').html(data.originalBL);
}
function setReauctionCancelDashboardCount(data) {
    $('#reauction-stock').html(data.reAuction);
    $('#cancel-stock').html(data.cancelStock);
}
function updateStockDetailsData(element, stockNo) {

    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        url: myContextPath + "/stock/details/data/" + stockNo,
        contentType: "application/json",
        success: function(data) {
            setStockDetailsData(element, data);

        }
    });

}

function setStockDetailsData(element, data) {
    var currentLocation = window.location.pathname;
    if (window.location.href.indexOf("sales") > -1) {
        $(element).find('.purchase').addClass('hidden');
        $(element).find('.reserved').removeClass('col-md-3').addClass('col-md-7');
    }
    if (data.data.exportSerial == null) {
        $(element).find('a#btn-export-certificate').addClass('hidden');
    } else {
        $(element).find('a#btn-export-certificate').attr('href', myContextPath + '/documents/tracking/export-certificate/' + data.data.stockNo + '.pdf');
    }
    $(element).find('a#btn-search-stock').attr('href', myContextPath + '/stock/stock-entry/' + ifNotValid(data.data.stockNo, '') + '?editFlag=1&return=' + currentLocation);


    var carouselContainer = $(element).find('#stock-images-carousel');
    for (var i = 0; i < data.data.imageUrls.length; i++) {
        if (i > 2) {
            $(element).find('#carousel-item').removeClass('hidden')
            $(element).find('#carousel-item>.item').clone().attr('data-index', i).appendTo(carouselContainer.find('.carousel-inner'));
        }
        var imageContainer = carouselContainer.find('div[data-index="' + i + '"].item');
        //set images
        //imageContainer.find('a').attr('href', myContextPath + '/downloadFile/' + images[i].diskFilename + '?path=' + images[i].diskDirectory + '/' + images[i].subDirectory + '&from=upload')
        //imageContainer.find('img').attr('src', myContextPath + '/downloadFile/' + images[i].diskFilename + '?path=' + images[i].diskDirectory + '/' + images[i].subDirectory + '&from=upload');
       
        imageContainer.find('img').attr('src', data.data.imageUrls[i]);
    }
    //init carousel
    $(element).find('#stock-images-carousel').carousel({
        interval: false
    })
    $(element).find('.carousel .item').each(function() {
        var next = $(this).next();
        if (!next.length) {
            next = $(this).siblings(':first');
        }
        next.children(':first-child').clone().appendTo($(this));

        if (next.next().length > 0) {
            next.next().children(':first-child').clone().appendTo($(this));
        } else {
            $(this).siblings(':first').children(':first-child').clone().appendTo($(this));
        }
    });
    //set attachments
    var attachments = data.data.attachments.filter(function(item) {
        return item.subDirectory == "document";
    });
    var attachmentContainer = $(element).find('#attachment-container');
    if (attachments.length > 0) {
        attachmentContainer.find('#no-attachment-msg').remove();
    }
    var attachment_tbody = attachmentContainer.find('table>tbody');

    for (var i = 0; i < attachments.length; i++) {
        var row = attachment_tbody.find('tr.clone').clone();
        row.find('td>i>span.filename').html(attachments[i].filename);
        row.find('td.description').html(attachments[i].description);
        //row.find('td.download>a').attr('href', myContextPath + '/downloadFile/' + attachments[i].diskFilename + '?path=' + attachments[i].diskDirectory + '/' + attachments[i].subDirectory + '&from=upload')
        row.removeClass('hidden clone')
        $(row).appendTo(attachment_tbody);
    }
    // stock images download as zip file
    $(element).find('span.downloadImages>a').attr('href', myContextPath + "/stock-image/zip/download?stockNo=" + ifNotValid(data.data.stockNo, ''));

    //set stock specifications
    $(element).find('#basic-info-container #stockNo').html(ifNotValid(data.data.stockNo, ''));
    $(element).find('#stockType').html(ifNotValid(data.data.isBidding == 0 ? 'Stock' : 'Bidding', ''));
    $(element).find('#biddingNo').html(ifNotValid(data.data.shuppinNo, ''));
    $(element).find('#chassisNo').html(ifNotValid(data.data.chassisNo, ''));
    $(element).find('#maker-model').html(ifNotValid(data.data.maker, '') + ' & ' + ifNotValid(data.data.model, ''));
    $(element).find('#manufactureYear').html(ifNotValid(data.data.manufactureYear, ''));
    $(element).find('#supplierName').html(ifNotValid(data.data.supplierName, '') + '/' + ifNotValid(data.data.auctionHouse, ''));
    $(element).find('#auctionHouse').html(ifNotValid(data.data.auctionHouse, ''));
    $(element).find('#sFirstRegDate').html(ifNotValid(data.data.firstRegDate, ''));
    $(element).find('#transmission').html(ifNotValid(data.data.transmission, ''));
    $(element).find('#manualTypes').html(ifNotValid(data.data.manualTypes, ''));
    $(element).find('#noOfDoors').html(ifNotValid(data.data.noOfDoors, ''));
    $(element).find('#noOfSeat').html(ifNotValid(data.data.noOfSeat, ''));
    $(element).find('#grade').html(ifNotValid(data.data.grade, ''));
    $(element).find('#auctionGrade').html(ifNotValid(data.data.auctionGrade, ''));
    $(element).find('#auctionGradeExt').html(ifNotValid(data.data.auctionGradeExt, ''));
    $(element).find('#auctionRemarks').html(ifNotValid(data.data.auctionRemarks, ''));
    $(element).find('#remarks').html(ifNotValid(data.data.remarks, ''));
    $(element).find('#fuel').html(ifNotValid(data.data.fuel, ''));
    $(element).find('#driven').html(ifNotValid(data.data.driven, ''));
    $(element).find('#mileage').html(ifNotValid(data.data.mileage, ''));
    $(element).find('#color').html(ifNotValid(data.data.color, ''));
    $(element).find('#cc').html(ifNotValid(data.data.cc, ''));
    $(element).find('#recycle').html(ifNotValid(data.data.recycle, ''));
    $(element).find('#numberPlate').html(ifNotValid(data.data.numberPlate, ''));
    $(element).find('#oldNumberPlate').html(ifNotValid(data.data.oldNumberPlate, ''));
    $(element).find('#equipment').html(ifNotValid(data.data.equipment, []).toString());
    $(element).find('#extraAccessories').html(ifNotValid(data.data.extraAccessories, []).toString());
    $(element).find('#destinationCountry').html(ifNotValid(data.data.destinationCountry, ''));
    $(element).find('#destinationPort').html(ifNotValid(data.data.destinationPort, ''));
    $(element).find('#reservedByName').html(ifNotValid(data.data.reservedByName, ''));
    $(element).find('#reservedCustomerName').html(ifNotValid(data.data.reservedCustomerName, ''));
    $(element).find('#reservedDate').html(ifNotValid(data.data.reservedDate, ''));

    $(element).find('#reservedPrice').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.reservedPrice, 0));
    $(element).find('#basePrice').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.minSellPrice, 0));

    $(element).find('#recycleAmount').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.recycleAmount, 0));
    $(element).find('#purchasedDate').html(ifNotValid(data.data.purchasedDate, ''));
    $(element).find('#purchaseCost').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.purchaseCost, 0));
    $(element).find('#commision').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.commision, 0));
    $(element).find('#roadTax').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.roadTax, 0));
    $(element).find('#otherCharges').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.otherCharges, 0));
    $(element).find('#total').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.total, 0));
    $(element).find('#totalTax').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.totalTax, 0));
    $(element).find('#totalTaxIncluded').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.totalTaxIncluded, 0));
    let shipmentType = '';
    if (ifNotValid(data.data.shipmentType, '') == 1) {
        shipmentType = 'RORO'
    } else if (ifNotValid(data.data.shipmentType, '') == 2) {
        shipmentType = 'CONTAINER'

    }
    $(element).find('#shipmentType').html(shipmentType);
    $(element).find('#currentLocation').html(ifNotValid(data.data.currentLocation, 'N/A'));
    var t_count = 0;
    var transportInfos = ifNotValid(data.data.transportInfos, []);
    var transportInfosTable = $(element).find('#transport-info-container>tbody');
    for (var i = 0; i < transportInfos.length; i++) {
        var pickupLocation = ifNotValid(transportInfos[i].pickupLocation, '').toUpperCase != 'OTHERS' ? ifNotValid(transportInfos[i].sPickupLocation, '') : ifNotValid(transportInfos[i].pickupLocationCustom, '');
        var dropLocation = ifNotValid(transportInfos[i].dropLocation, '').toUpperCase != 'OTHERS' ? ifNotValid(transportInfos[i].sDropLocation, '') : ifNotValid(transportInfos[i].dropLocationCustom, '');
        var transporter = ifNotValid(transportInfos[i].transporter, '');
        var charge = ifNotValid(transportInfos[i].charge, '');
        var etd = ifNotValid(transportInfos[i].etd, '');
        if (isEmpty(pickupLocation) && isEmpty(dropLocation) && isEmpty(transporter) && isEmpty(charge) && isEmpty(etd)) {
            continue;
        }
        var row = transportInfosTable.find('tr:first').clone();
        row.find('td.sno').html(++t_count);
        row.find('td.from').html(pickupLocation);
        row.find('td.to').html(dropLocation);
        row.find('td.transporter').html(transporter);
        row.find('td.charge').html(charge);
        row.find('td.etd').html(etd);
        row.removeClass('hidden');
        row.appendTo(transportInfosTable);

    }
    var inspectionInfos = ifNotValid(data.data.inspectionInfos, []);
    var inspectionInfosTable = $(element).find('#inspection-info-container>tbody');
    var i_count = 0;
    for (var i = 0; i < inspectionInfos.length; i++) {
        var inspectionSentDate = ifNotValid(inspectionInfos[i].inspectionSentDate, '');
        var country = ifNotValid(inspectionInfos[i].country, '')
        var inspectedDate = ifNotValid(inspectionInfos[i].dateOfIssue, '')
        var forwarder = ifNotValid(inspectionInfos[i].forwarder)
        if (isEmpty(inspectionSentDate) && isEmpty(country) && isEmpty(inspectedDate) && isEmpty(forwarder)) {
            continue;
        }
        var forwarderInspectionCompany;
        if (inspectionInfos[i].inspectionCompanyFlag == 0) {
            forwarderInspectionCompany = ifNotValid(inspectionInfos[i].inspectionCompany, '')

        } else if (inspectionInfos[i].inspectionCompanyFlag == 1) {
            forwarderInspectionCompany = ifNotValid(inspectionInfos[i].forwarder, '')
        }
        var row = inspectionInfosTable.find('tr:first').clone();
        row.find('td.sno').html(++i_count);
        row.find('td.sendDate').html(inspectionSentDate);
        row.find('td.country').html(country);
        row.find('td.inspectionDate').html(inspectedDate);
        row.find('td.forwarder-inspection-company').html(forwarderInspectionCompany);
        row.removeClass('hidden');
        row.appendTo(inspectionInfosTable);

    }

}
function getUnique(arr, comp) {

    const unique = arr.map(e=>e[comp])// store the keys of the unique objects
    .map((e,i,final)=>final.indexOf(e) === i && i)// eliminate the dead keys & store unique objects
    .filter(e=>arr[e]).map(e=>arr[e]);

    return unique;
}
function unique(list) {
    var result = [];
    $.each(list, function(i, e) {
        if ($.inArray(e, result) == -1)
            result.push(e);
    });
    return result;
}
function hasDuplicates(array) {
    return (new Set(array)).size !== array.length;
}
//for select2 custom search match
function matchStart(params, data) {
    params.term = params.term || '';
    if (data.text.toUpperCase().indexOf(params.term.toUpperCase()) == 0) {
        return data;
    }
    return false;
}
function getAutonumericValue(element) {
    return $(element).autoNumeric('init').autoNumeric('get');
}
function setAutonumericValue(element, val) {
    $(element).autoNumeric('init').autoNumeric('set', val);
}
function alertMessage(alertEle, message) {
    $(alertEle).css('display', '').html(message);
    $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
        $(alertEle).slideUp(500);
    });
}
function post_to_url(path, params, method) {
    method = method || "post";

    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for (var key in params) {
        if (params.hasOwnProperty(key)) {

            if (params[key]instanceof Array) {
                for (let i = 0; i < params[key].length; i++) {
                    var hiddenField = document.createElement("input");
                    hiddenField.setAttribute("type", "hidden");
                    hiddenField.setAttribute("name", key + '[]');
                    hiddenField.setAttribute("value", params[key][i]);
                    form.appendChild(hiddenField);
                }

            } else {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);
                form.appendChild(hiddenField);
            }

        }
    }
    document.body.appendChild(form);
    form.submit();
    form.remove();
}
// $.ajaxSetup({
//     statusCode: {
//         401: function() {
//             window.location.href = myContextPath + "/error/access-denied";
//         },
//         404: function() {
//             window.location.href = myContextPath + "/error/access-denied";
//         },
//         500: function() {
//             window.location.href = myContextPath + "/error/500";
//         }
//     }
// });
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
    if (jqxhr.status != 200 && jqxhr.status != 0) {
        if (jqxhr.status == 401 || jqxhr.status == 404) {
            window.location.href = myContextPath + "/error/access-denied";
        } else if (jqxhr.status != 500) {
            window.location.href = myContextPath + "/error/500";
        }
    }
});

// function noBack() {
//     window.history.forward();
// }
