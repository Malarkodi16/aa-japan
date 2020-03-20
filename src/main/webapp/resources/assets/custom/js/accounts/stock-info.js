var mobileNo, city;
$(function() {

    $('table>tbody').find('tr>th').css("background-color", "lightblue");
    $('table>thead').find('tr>th').css("background-color", "lightblue")
    var stockNo = $('input[name="stockNo"]').val();
    if (!isEmpty(stockNo)) {
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/stock/info/" + stockNo,
            contentType: "application/json",
            success: function(data) {
                $('#transport-info-container>tbody>tr').not(':first').remove();
                $('#inspection-info-container>tbody>tr').not(':first').remove();
                $('#shipping-info-container>tbody>tr').not(':first').remove();
                $('#document-conversion-info-container>tbody>tr').not(':first').remove();
                $('#sales-invoice-info-container>tbody>tr').not(':first').remove();
                setStockDetailsData(data.data);

            }
        });
    }
    $('#stockNo').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    $('#btn-search-stock').click(function() {
        let searchStockNo = $('select#stockNo').val();
        if (!isEmpty(searchStockNo)) {
            $.redirect(myContextPath + '/shipping/stockInfo', {
                stockNo: searchStockNo
            }, "GET");
        }

    })

    function setStockDetailsData(data) {
        $('#spinner').hide();

        var carouselContainer = $('#stock-images-carousel');

        for (var i = 0; i < data.imageUrls.length; i++) {
            if (i > 2) {
                $('#carousel-item').removeClass('hidden')
                $('#carousel-item>.item').clone().attr('data-index', i).appendTo(carouselContainer.find('.carousel-inner'));
            }
            var imageContainer = carouselContainer.find('div[data-index="' + i + '"].item');
            //set images
            //imageContainer.find('a').attr('href', myContextPath + '/downloadFile/' + images[i].diskFilename + '?path=' + images[i].diskDirectory + '/' + images[i].subDirectory + '&from=upload')
            //imageContainer.find('img').attr('src', myContextPath + '/downloadFile/' + images[i].diskFilename + '?path=' + images[i].diskDirectory + '/' + images[i].subDirectory + '&from=upload');

            imageContainer.find('img').attr('src', data.imageUrls[i]);
        }
        //init carousel
        $('#stock-images-carousel').carousel({
            interval: 10000
        })
        $('.carousel .item').each(function() {
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

        $('#downloadImages').attr('href', myContextPath + "/stock-image/zip/download?stockNo=" + ifNotValid(data.stockNo, ''));

        if (!isEmpty(data.baseInfo)) {
            if (data.baseInfo.stockNo != null) {
                $('#stockNoValue').text(data.baseInfo.stockNo);
            }
            if (data.baseInfo.chassisNo != null) {
                $('#chassisNo').text(data.baseInfo.chassisNo);
            }
            if (data.baseInfo.firstRegDate != null) {
                $('#firstRegDate').text(data.baseInfo.firstRegDate);
            }
            if (data.baseInfo.maker != null) {
                $('#maker').text(data.baseInfo.maker);
            }
            if (data.baseInfo.model != null) {
                $('#model').text(data.baseInfo.model);
            }
            if (data.baseInfo.category != null) {
                $('#category').text(data.baseInfo.category);
            }
            if (data.baseInfo.subcategory != null) {
                $('#subcategory').text(data.baseInfo.subcategory);
            }
            if (data.baseInfo.modelType != null) {
                $('#modelType').text(data.baseInfo.modelType);
            }
            if (data.baseInfo.extraEquipments != null) {
                $('#extraEquipments').text(data.baseInfo.extraEquipments);
            }
            if (data.baseInfo.tyreSize != null) {
                $('#tyreSize').text(data.baseInfo.tyreSize);
            }
            if (data.baseInfo.craneType != null) {
                $('#craneType').text(data.baseInfo.craneType);
            }
            if (data.baseInfo.craneCut != null) {
                $('#craneCut').text(data.baseInfo.craneCut);
            }
            if (data.baseInfo.exel != null) {
                $('#exel').text(data.baseInfo.exel);
            }
            if (data.baseInfo.tankKiloLitre != null) {
                $('#tankKiloLitre').text(data.baseInfo.tankKiloLitre);
            }
            if (data.baseInfo.grade != null) {
                $('#grade').text(data.baseInfo.grade);
            }
            if (data.baseInfo.auctionGrade != null) {
                $('#auctionGrade').text(data.baseInfo.auctionGrade);
            }
            if (data.baseInfo.auctionGradeExt != null) {
                $('#auctionGradeExt').text(data.baseInfo.auctionGradeExt);
            }
            if (data.baseInfo.transmission != null) {
                $('#transmission').text(data.baseInfo.transmission);
            }
            if (data.baseInfo.manualTypes != null) {
                $('#manualTypes').text(data.baseInfo.manualTypes);
            }
            if (data.baseInfo.noOfSeat != null) {
                $('#noOfSeat').text(data.baseInfo.noOfSeat);
            }
            if (data.baseInfo.noOfDoors != null) {
                $('#noOfDoors').text(data.baseInfo.noOfDoors);
            }
            if (data.baseInfo.fuel != null) {
                $('#fuel').text(data.baseInfo.fuel);
            }
            if (data.baseInfo.driven != null) {
                $('#driven').text(data.baseInfo.driven);
            }
            if (data.baseInfo.mileage != null) {
                $('#mileage').text(data.baseInfo.mileage);
            }
            if (data.baseInfo.color != null) {
                $('#color').text(data.baseInfo.color);
            }
            if (data.baseInfo.cc != null) {
                $('#cc').text(data.baseInfo.cc);
            }
            if (data.baseInfo.recycle != null) {
                $('#recycle').text(data.baseInfo.recycle);
            }
            if (data.baseInfo.numberPlate != null) {
                $('#numberPlate').text(data.baseInfo.numberPlate);
            }
            if (data.baseInfo.oldNumberPlate != null) {
                $('#oldNumberPlate').text(data.baseInfo.oldNumberPlate);
            }
            if (data.baseInfo.optionDescription != null) {
                $('#optionDescription').text(data.baseInfo.optionDescription);
            }
            if (data.baseInfo.remarks != null) {
                $('#remarks').text(data.baseInfo.remarks);
            }
            if (data.baseInfo.auctionRemarks != null) {
                $('#auctionRemarks').text(data.baseInfo.auctionRemarks);
            }
            if (data.baseInfo.equipment != null) {
                $('#equipment').text(data.baseInfo.equipment);
            }
            if (data.baseInfo.v != null) {
                $('#extraAccessories').text(data.baseInfo.extraAccessories);
            }
            if (data.baseInfo.purchaseType != null) {
                $('#purchaseType').text(data.baseInfo.purchaseType);
            }
            if (data.baseInfo.purchaseDate != null) {
                $('#purchaseDate').text(data.baseInfo.purchaseDate);
            }
            if (data.baseInfo.lotNo != null) {
                $('#lotNo').text(data.baseInfo.lotNo);
            }
            if (data.baseInfo.posNo != null) {
                $('#posNo').text(data.baseInfo.posNo);
            }
            if (data.baseInfo.fob != null) {
                $('#fob').text(data.baseInfo.fob);
            }
            if (data.baseInfo.buyingPrice != null) {
                $('#buyingPrice').text(data.baseInfo.buyingPrice);
            }
            if (data.baseInfo.manufactureYear != null) {
                $('#manufactureYear').text(data.baseInfo.manufactureYear);
            }
            if (data.baseInfo.m3 != null) {
                $('#m3').text(data.baseInfo.m3);
            }
            if (data.baseInfo.length != null) {
                $('#length').text(data.baseInfo.length);
            }
            if (data.baseInfo.width != null) {
                $('#width').text(data.baseInfo.width);
            }
            if (data.baseInfo.height != null) {
                $('#height').text(data.baseInfo.height);
            }
            if (data.baseInfo.weight != null) {
                $('#weight').text(data.baseInfo.weight);
            }
            if (data.baseInfo.offerPrice != null) {
                $('#offerPrice').text(data.baseInfo.offerPrice);
            }
            if (data.baseInfo.minSellingPriceInDollar != null) {
                $('#minSellingPriceInDollar').text(data.baseInfo.minSellingPriceInDollar);
            }
            if (data.baseInfo.unit != null) {
                $('#unit').text(data.baseInfo.unit);
            }
            if (data.baseInfo.reserveTextValue != null) {
                $('#reserveTextValue').text(data.baseInfo.reserveTextValue);
            }
            if (data.baseInfo.statusTextValue != null) {
                $('#statusTextValue').text(data.baseInfo.statusTextValue);
            }
            if (data.baseInfo.lcStatusTextValue != null) {
                $('#lcStatusTextValue').text(data.baseInfo.lcStatusTextValue);
            }
            if (data.baseInfo.isLockedTextValue != null) {
                $('#isLockedTextValue').text(data.baseInfo.isLockedTextValue);
            }
            if (data.baseInfo.showForSalesTextValue != null) {
                $('#showForSalesTextValue').text(data.baseInfo.showForSalesTextValue);
            }
            if (data.baseInfo.accountTextValue != null) {
                $('#accountTextValue').text(data.baseInfo.accountTextValue);
            }
            if (data.baseInfo.isMovableTextValue != null) {
                $('#isMovableTextValue').text(data.baseInfo.isMovableTextValue);
            }
            if (data.baseInfo.isBiddingTextValues != null) {
                $('#isBiddingTextValues').text(data.baseInfo.isBiddingTextValues);
            }
            if (data.baseInfo.shippingStatusTextValue != null) {
                $('#shippingStatusTextValue').text(data.baseInfo.shippingStatusTextValue);
            }
            if (data.baseInfo.inspectionStatusTextValue != null) {
                $('#inspectionStatusTextValue').text(data.baseInfo.inspectionStatusTextValue);
            }
            if (data.baseInfo.shipmentTypeTextValue != null) {
                $('#shipmentTypeTextValue').text(data.baseInfo.shipmentTypeTextValue);
            }
            if (data.baseInfo.isPhotoUploadedTextValue != null) {
                $('#isPhotoUploadedTextValue').text(data.baseInfo.isPhotoUploadedTextValue);
            }
            if (data.baseInfo.transportationStatusTextValue != null) {
                $('#transportationStatusTextValue').text(data.baseInfo.transportationStatusTextValue);
            }
            if (data.baseInfo.shippingInstructionStatusTextValue != null) {
                $('#shippingInstructionStatusTextValue').text(data.baseInfo.shippingInstructionStatusTextValue);
            }
            if (data.baseInfo.lockedBySalesPersonName != null) {
                $('#lockedBySalesPersonName').text(data.baseInfo.lockedBySalesPersonName);
            }
        }

        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionHouse != null) {
            $('#auctionHouse').text(data.purchaseDetail.auctionHouse);
        }
        if (data.purchaseDetail.purchaseStatus != null) {
            if (data.purchaseDetail.purchaseStatus == 0)
                $('#invoiceStatus').text("Invoice Not Booked");
            else if (data.purchaseDetail.purchaseStatus == 1)
                $('#invoiceStatus').text("Invoice Booked");

            else if (data.purchaseDetail.purchaseStatus == 2)
                $('#invoiceStatus').text("Invoice Approved");

            else if (data.purchaseDetail.purchaseStatus == 6)
                $('#invoiceStatus').text("Payment ");

            else if (data.purchaseDetail.purchaseStatus == 4)
                $('#invoiceStatus').text("Rauction Invoice Booked");

            else if (data.purchaseDetail.purchaseStatus == 1)
                $('#invoiceStatus').text("Cancel Invoice Booked");
        }
        if (data.purchaseDetail.invoiceNo != null) {
            $('#invoiceNo').text(data.purchaseDetail.invoiceNo);
        }
        if (data.purchaseDetail.auctionRefNo != null) {
            $('#refNo').text(data.purchaseDetail.auctionRefNo);
        }
        if (data.purchaseDetail.invoiceDate != null) {
            $('#invoiceDate').text(data.purchaseDetail.invoiceDate);
        }
        if (data.purchaseDetail.dueDate != null) {
            $('#dueDate').text(data.purchaseDetail.dueDate);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }
        if (data.purchaseDetail.auctionCompany != null) {
            $('#auctionCompany').text(data.purchaseDetail.auctionCompany);
        }

        var transportInfosTable = $('#transport-info-container>tbody');

        var t_count = 0;
        if (!isEmpty(data.transportDetail)) {

            $('#currentLocation').html(ifNotValid(data.transportDetail.currentLocation, 'N/A').toUpperCase != 'OTHERS' ? ifNotValid(data.transportDetail.currentLocation, '') : ifNotValid(data.transportDetail.currrentLocationCustom, ''));
            let transportStatus = "";
            let invoiceStatus = "";
            for (var i = 0; i < data.transportDetail.transGroupInfo.length; i++) {
                var transporter = ifNotValid(data.transportDetail.transGroupInfo[i].transporter, '');
                var fromLocation = ifNotValid(data.transportDetail.transGroupInfo[i].fromLocation, '').toUpperCase != 'OTHERS' ? ifNotValid(data.transportDetail.transGroupInfo[i].fromLocation, '') : ifNotValid(data.transportDetail.transGroupInfo[i].fromLocationCustom, '');
                var toLocation = ifNotValid(data.transportDetail.transGroupInfo[i].toLocation, '').toUpperCase != 'OTHERS' ? ifNotValid(data.transportDetail.transGroupInfo[i].toLocation, '') : ifNotValid(data.transportDetail.transGroupInfo[i].toLocationCustom, '');

                var estimatedAmount = ifNotValid(data.transportDetail.transGroupInfo[i].estimatedAmount, '');
                var actualAmount = ifNotValid(data.transportDetail.transGroupInfo[i].actualAmount, '');

                if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 0)
                    transportStatus = "NEW_ITEM";
                else if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 1)
                    transportStatus = "ITEM_INITIATED";
                else if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 2)
                    transportStatus = "ITEM_CONFIRMED";
                else if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 3)
                    transportStatus = "ITEM_CANCELLED";
                else if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 4)
                    transportStatus = "ITEM_REARRANGE";
                else if (data.transportDetail.transGroupInfo[i].transportOrdertatus == 5)
                    transportStatus = "ITEM_DELIVERED";

                if (data.transportDetail.transGroupInfo[i].transportInvoiceStatus == 0)
                    invoiceStatus = "INVOICE_INITIATED";
                else if (data.transportDetail.transGroupInfo[i].transportInvoiceStatus == 1)
                    invoiceStatus = "INVOICE_CREATED";
                else if (data.transportDetail.transGroupInfo[i].transportInvoiceStatus == 2)
                    invoiceStatus = "INVOICE_CANCELED";

                var row = transportInfosTable.find('tr:first').clone();
                //row.find('td').html('');
                row.find('td.sno').html(++t_count);
                row.find('td.transporter').html(transporter);
                row.find('td.from').html(fromLocation);
                row.find('td.to').html(toLocation);
                row.find('td.transportStatus').html(transportStatus);
                row.find('td.invoiceStatus').html(invoiceStatus);
                row.find('td.estimatedAmount').html(estimatedAmount);
                row.find('td.actualAmount').html(actualAmount);
                row.removeClass('hidden');
                row.appendTo(transportInfosTable);

            }
            $('#lastTransportStatus').html(transportStatus);
            $('#noOfTransportArranged').html(data.transportDetail.transportationCount);
            $('#lastTransportInvoiceStatus').html(invoiceStatus);
            $('#transportInvoiceRefNo').html(data.transportDetail.invoiceRefNo);
            $('#transportRefNo').html(data.transportDetail.refNo);
            $('#transportInvoiceDate').html(data.transportDetail.invoiceDate);
            $('#transportDueDate').html(data.transportDetail.dueDate);
        }

        var inspectionInfosTable = $('#inspection-info-container>tbody');

        var i_count = 0;
        if (!isEmpty(data.inspectionDetail)) {

            $('#inspectionCountry').html(data.inspectionDetail.inspectionCountry);
            let status = "";
            let inspectionInvoiceStatus = "";
            let docStatus = "";
            if (data.inspectionDetail.inspectionDocStatus == 0)
                docStatus = "NOT_SENT"
            else if (data.inspectionDetail.inspectionDocStatus == 1)
                docStatus = "COPY_SENT"
            else if (data.inspectionDetail.inspectionDocStatus == 2)
                docStatus = "ORIGINAL_SENT"
            for (var i = 0; i < data.inspectionDetail.inspectionGroupInfo.length; i++) {
                let status = "";
                let inspectionInvoiceStatus = "";
                var inspectionCompany = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].inspectionCompany, '');
                var destCountry = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].destCountry, '');
                var forwarder = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].forwarder, '');
                var estimatedAmount = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].estimatedAmount, '');
                var actualAmount = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].actualAmount, '');
                var inspectionCompanyFlag = ifNotValid(data.inspectionDetail.inspectionGroupInfo[i].inspectionCompanyFlag, '');
                if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 0)
                    inspectionInvoiceStatus = "INITIATED";
                else if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 2)
                    inspectionInvoiceStatus = "CANCELLED";
                else if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 3)
                    inspectionInvoiceStatus = "COMPLETE";
                else if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 4)
                    inspectionInvoiceStatus = "REARRANGE";
                else if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 5)
                    inspectionInvoiceStatus = "PASSED";
                else if (data.inspectionDetail.inspectionGroupInfo[i].invoiceStatus == 6)
                    inspectionInvoiceStatus = "DELETED";

                if (data.inspectionDetail.inspectionGroupInfo[i].status == 0)
                    status = "IDLE";
                else if (data.inspectionDetail.inspectionGroupInfo[i].status == 1)
                    status = "CREATED";

                var row = inspectionInfosTable.find('tr:first').clone();
                //row.find('td').html('');
                row.find('td.sno').html(++i_count);
                row.find('td.forwarder-inspection-company').html(inspectionCompanyFlag == 0 ? inspectionCompany : forwarder);
                row.find('td.dest-country').html(destCountry);
                row.find('td.status').html(status);
                row.find('td.inspectionInvoiceStatus').html(inspectionInvoiceStatus);
                row.find('td.estimatedAmount').html(estimatedAmount);
                row.find('td.actualAmount').html(actualAmount);
                row.removeClass('hidden');
                row.appendTo(inspectionInfosTable);

            }
            $('#inspectionStatus').html(inspectionInvoiceStatus);
            $('#inspectionSentDate').html(data.inspectionDetail.inspectionSentDate);
            $('#inspectionDocStatus').html(docStatus);
            $('#inspectionDate').html(data.inspectionDetail.inspectionDate);
            $('#inspectionDocSentDate').html(data.inspectionDetail.inspectionDocSentDate);
            $('#dateOfIssue').html(data.inspectionDetail.dateOfIssue);
            $('#certificateNo').html(data.inspectionDetail.certificateNo);
            $('#inspectionCompany').html(data.inspectionDetail.inspectionCompany);
        }

        var shippingInfosTable = $('#shipping-info-container>tbody');

        var i_count = 0;
        if (!isEmpty(data.shippingDetail)) {
            for (var i = 0; i < data.shippingDetail.shippingGroupInfo.length; i++) {
                let shippingStatus = "";
                let invoiceStatus = "";
                var forwarder = ifNotValid(data.shippingDetail.shippingGroupInfo[i].forwarder, '');
                var originPort = ifNotValid(data.shippingDetail.shippingGroupInfo[i].originPort, '');
                var destinationCountry = ifNotValid(data.shippingDetail.shippingGroupInfo[i].destinationCountry, '');
                var destinationPort = ifNotValid(data.shippingDetail.shippingGroupInfo[i].destinationPort, '');
                var destinationYard = ifNotValid(data.shippingDetail.shippingGroupInfo[i].destinationYard, '');
                var shippingType = ifNotValid(data.shippingDetail.shippingGroupInfo[i].shippingType, '');
                var vesselName = ifNotValid(data.shippingDetail.shippingGroupInfo[i].shipName + '[' + data.shippingDetail.shippingGroupInfo[i].shippingCompany + ']', '');
                var containerNo = ifNotValid(data.shippingDetail.shippingGroupInfo[i].containerNo, '');
                var containerName = ifNotValid(data.shippingDetail.shippingGroupInfo[i].containerName, '');
                var shippingBlNo = ifNotValid(data.shippingDetail.shippingGroupInfo[i].shippingBlNo, '');
                var slaNo = ifNotValid(data.shippingDetail.shippingGroupInfo[i].slaNo, '');
                if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 0)
                    shippingStatus = "INITIATED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 1)
                    shippingStatus = "ACCEPTED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 2)
                    shippingStatus = "RESCHEDULED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 3)
                    shippingStatus = "CANCELLED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 4)
                    shippingStatus = "CONTAINER_CONFIRMED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 5)
                    shippingStatus = "VESSEL_CONFIRMED";
                else if (data.shippingDetail.shippingGroupInfo[i].shippingStatus == 6)
                    shippingStatus = "UPDATED_BL_&_SHIPPED";

                if (data.shippingDetail.shippingGroupInfo[i].invoiceStatus == 0)
                    invoiceStatus = "NOT_CREATED";
                else if (data.shippingDetail.shippingGroupInfo[i].invoiceStatus == 1)
                    invoiceStatus = "CREATED";

                var row = shippingInfosTable.find('tr:first').clone();
                //row.find('td').html('');
                row.find('td.sno').html(++i_count);
                row.find('td.forwarder').html(forwarder);
                row.find('td.originPort').html(originPort);
                row.find('td.destinationCountry').html(destinationCountry);
                row.find('td.destinationPort').html(destinationPort);
                row.find('td.destinationYard').html(destinationYard);
                row.find('td.type').html(shippingType == 1 ? "RORO" : "CONTAINER");
                row.find('td.vessel').html(vesselName);
                row.find('td.shippingStatus').html(shippingStatus);
                row.find('td.invoiceStatus').html(invoiceStatus);
                row.find('td.containerNo').html(containerNo);
                row.find('td.containerName').html(containerName);
                row.find('td.shippingBlNo').html(shippingBlNo);
                row.find('td.slaNo').html(slaNo);
                row.removeClass('hidden');
                row.appendTo(shippingInfosTable);

            }
        }

        if (data.documentInfo.documentRecivedStatusTextValue != null) {
            $('#documentRecivedStatusTextValue').text(data.documentInfo.documentRecivedStatusTextValue);
        }
        if (data.documentInfo.plateNoReceivedDate != null) {
            $('#plateNoReceivedDate').text(data.documentInfo.plateNoReceivedDate);
        }
        if (data.documentInfo.documentTypeTextValue != null) {
            $('#documentTypeTextValue').text(data.documentInfo.documentTypeTextValue);
        }
        if (data.documentInfo.documentReceivedDate != null) {
            $('#documentReceivedDate').text(data.documentInfo.documentRecivedStatusTextValue);
        }
        if (data.documentInfo.documentConvertToTextValue != null) {
            $('#documentConvertToTextValue').text(data.documentInfo.documentConvertToTextValue);
        }
        if (data.documentInfo.documentFob != null) {
            $('#documentFob').text(data.documentInfo.documentFob);
        }
        if (data.documentInfo.documentConvertedDate != null) {
            $('#documentConvertedDate').text(data.documentInfo.documentConvertedDate);
        }
        if (data.documentInfo.handoverToTextValue != null) {
            $('#handoverToTextValue').text(data.documentInfo.handoverToTextValue);
        }
        if (data.documentInfo.handoverToUserName != null) {
            $('#handoverToUserName').text(data.documentInfo.handoverToUserName);
        }
        if (data.documentInfo.rikujiStatusValueText != null) {
            $('#rikujiStatusValueText').text(data.documentInfo.rikujiStatusValueText);
        }
        if (data.documentInfo.rikujiUpdateToOneDate != null) {
            $('#rikujiUpdateToOneDate').text(data.documentInfo.rikujiUpdateToOneDate);
        }
        if (data.documentInfo.rikujiRemarks != null) {
            $('#rikujiRemarks').text(data.documentInfo.rikujiRemarks);
        }

        var documentInfosTable = $('#document-conversion-info-container>tbody');

        var i_count = 0;
        if (!isEmpty(data.documentInfo.documentConversionDtos)) {
            for (var i = 0; i < data.documentInfo.documentConversionDtos.length; i++) {
                var docConvertToTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].docConvertToTextValue, '');
                var exportCertificateStatusTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].exportCertificateStatusTextValue, '');
                var shippingCompanyName = ifNotValid(data.documentInfo.documentConversionDtos[i].shippingCompanyName, '');
                var inspectionCompanyName = ifNotValid(data.documentInfo.documentConversionDtos[i].inspectionCompanyName, '');
                var docOriginalSentTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].docOriginalSentTextValue, '');
                var docEmailSentTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].docEmailSentTextValue, '');
                var docReceivedStatusTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].docReceivedStatusTextValue, '');
                var handoverStatusTextValue = ifNotValid(data.documentInfo.documentConversionDtos[i].handoverStatusTextValue, '');
                var docSendDate = ifNotValid(data.documentInfo.documentConversionDtos[i].docSendDate, '');
                var reauctionDate = ifNotValid(data.documentInfo.documentConversionDtos[i].reauctionDate, '');

                var row = documentInfosTable.find('tr:first').clone();
                //row.find('td').html('');
                row.find('td.sno').html(++i_count);
                row.find('td.docConvertToTextValue').html(docConvertToTextValue);
                row.find('td.exportCertificateStatusTextValue').html(exportCertificateStatusTextValue);
                row.find('td.shippingCompanyName').html(shippingCompanyName);
                row.find('td.inspectionCompanyName').html(inspectionCompanyName);
                row.find('td.docOriginalSentTextValue').html(docOriginalSentTextValue);
                row.find('td.docEmailSentTextValue').html(docEmailSentTextValue);
                row.find('td.docReceivedStatusTextValue').html(docReceivedStatusTextValue);
                row.find('td.handoverStatusTextValue').html(handoverStatusTextValue);
                row.find('td.docSendDate').html(docSendDate);
                row.find('td.reauctionDate').html(reauctionDate);
                row.removeClass('hidden');
                row.appendTo(documentInfosTable);

            }
        }

        var salesInfosTable = $('#sales-invoice-info-container>tbody');

        var i_count = 0;
        if (!isEmpty(data.documentInfo.salesInvoices)) {
            for (var i = 0; i < data.documentInfo.salesInvoices.length; i++) {
                var invoiceNo = ifNotValid(data.documentInfo.salesInvoices[i].invoiceNo, '');
                var customerName = ifNotValid(data.documentInfo.salesInvoices[i].customerName, '');
                var customerType = ifNotValid(data.documentInfo.salesInvoices[i].customerType, '');
                var consigneeName = ifNotValid(data.documentInfo.salesInvoices[i].consigneeName, '');
                var notifypartyName = ifNotValid(data.documentInfo.salesInvoices[i].notifypartyName, '');
                var currencyTypeTextValue = ifNotValid(data.documentInfo.salesInvoices[i].currencyTypeTextValue, '');
                var exchangeRate = ifNotValid(data.documentInfo.salesInvoices[i].exchangeRate, '');
                var total = ifNotValid(data.documentInfo.salesInvoices[i].total, '');
                var statusTextValue = ifNotValid(data.documentInfo.salesInvoices[i].statusTextValue, '');
                var salesPersonIdName = ifNotValid(data.documentInfo.salesInvoices[i].salesPersonIdName, '');
                var amountAllocatted = ifNotValid(data.documentInfo.salesInvoices[i].amountAllocatted, '');
                var amountReceived = ifNotValid(data.documentInfo.salesInvoices[i].amountReceived, '');

                var row = salesInfosTable.find('tr:first').clone();
                //row.find('td').html('');
                row.find('td.sno').html(++i_count);
                row.find('td.invoiceNo').html(invoiceNo);
                row.find('td.customerName').html(customerName);
                row.find('td.customerType').html(customerType);
                row.find('td.consigneeName').html(consigneeName);
                row.find('td.notifypartyName').html(notifypartyName);
                row.find('td.currencyTypeTextValue').html(currencyTypeTextValue);
                row.find('td.exchangeRate').html(exchangeRate);
                row.find('td.total').html(total);
                row.find('td.statusTextValue').html(statusTextValue);
                row.find('td.salesPersonIdName').html(salesPersonIdName);
                row.find('td.amountAllocatted').html(amountAllocatted);
                row.find('td.amountReceived').html(amountReceived);
                row.removeClass('hidden');
                row.appendTo(salesInfosTable);

            }
        }

    }

})
