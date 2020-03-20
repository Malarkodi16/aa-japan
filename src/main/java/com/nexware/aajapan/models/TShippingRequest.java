package com.nexware.aajapan.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.utils.AppUtil;

@Document(collection = "t_shppng_rqust")
public class TShippingRequest extends EntityModelBase {
	@Id
	private String id;
	@Indexed(unique = true)
	private String shipmentRequestId;// auto generate
	@Indexed
	private String allocationId;// auto generate
	private String stockNo;// refer t_stck
	private int toFlag;
	private String forwarderId;// refer m_frwdr
	private String orginCountry;// refer m_cntry_prt
	private String orginPort;// refer m_cntry_prt
	private String destCountry;// refer m_cntry_prt
	private String destPort;// refer m_cntry_prt
	private String yard;
	private String scheduleId;
	private Integer shippingType;
	private String dhlNo;
	private Integer status;
	private Integer invoiceStatus;
	private Integer shippingStatus;
	private String shippingInstructionId;
	private Integer blDraftStatus;
	private Integer blOriginalStatus;
	private String containerNo;
	private String containerName;
	private String slaNo;
	private String blNo;
	private Integer freightCompletionStatus;
	private Integer freightChargeStatus;
	private Integer freightUsdChargeStatus;
	private Integer shippingChargeStatus;
	private Integer inspectionChargeStatus;
	private Integer radiationChargeStatus;
	private String remarks;
	private Integer recSurStatus;
	private Integer blDocumentStatus;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date recSurDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date blDocReceivedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date blDocIssuedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date blDocDispatchedDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date blDocSurrenderedDate;

	private String yardId;
	private Double m3;
	private Double length;
	private Double width;
	private Double height;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShipmentRequestId() {
		return shipmentRequestId;
	}

	public void setShipmentRequestId(String shipmentRequestId) {
		this.shipmentRequestId = shipmentRequestId;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getOrginCountry() {
		return orginCountry;
	}

	public void setOrginCountry(String orginCountry) {
		this.orginCountry = orginCountry;
	}

	public String getOrginPort() {
		return orginPort;
	}

	public void setOrginPort(String orginPort) {
		this.orginPort = orginPort;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getShippingInstructionId() {
		return shippingInstructionId;
	}

	public void setShippingInstructionId(String shippingInstructionId) {
		this.shippingInstructionId = shippingInstructionId;
	}

	public Integer getShippingType() {
		return shippingType;
	}

	public void setShippingType(Integer shippingType) {
		this.shippingType = shippingType;
	}

	public Integer getBlDraftStatus() {
		return blDraftStatus;
	}

	public void setBlDraftStatus(Integer blDraftStatus) {
		this.blDraftStatus = blDraftStatus;
	}

	public Integer getBlOriginalStatus() {
		return blOriginalStatus;
	}

	public void setBlOriginalStatus(Integer blOriginalStatus) {
		this.blOriginalStatus = blOriginalStatus;
	}

	public String getDhlNo() {
		return dhlNo;
	}

	public void setDhlNo(String dhlNo) {
		this.dhlNo = dhlNo;
	}

	public int getToFlag() {
		return toFlag;
	}

	public void setToFlag(int toFlag) {
		this.toFlag = toFlag;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getYard() {
		return yard;
	}

	public void setYard(String yard) {
		this.yard = yard;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getSlaNo() {
		return slaNo;
	}

	public void setSlaNo(String slaNo) {
		this.slaNo = slaNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public Integer getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Integer getFreightCompletionStatus() {
		return freightCompletionStatus;
	}

	public void setFreightCompletionStatus(Integer freightCompletionStatus) {
		this.freightCompletionStatus = freightCompletionStatus;
	}

	public Integer getFreightChargeStatus() {
		return freightChargeStatus;
	}

	public void setFreightChargeStatus(Integer freightChargeStatus) {
		this.freightChargeStatus = freightChargeStatus;
	}

	public Integer getFreightUsdChargeStatus() {
		return freightUsdChargeStatus;
	}

	public void setFreightUsdChargeStatus(Integer freightUsdChargeStatus) {
		this.freightUsdChargeStatus = freightUsdChargeStatus;
	}

	public Integer getShippingChargeStatus() {
		return shippingChargeStatus;
	}

	public void setShippingChargeStatus(Integer shippingChargeStatus) {
		this.shippingChargeStatus = shippingChargeStatus;
	}

	public Integer getInspectionChargeStatus() {
		return inspectionChargeStatus;
	}

	public void setInspectionChargeStatus(Integer inspectionChargeStatus) {
		this.inspectionChargeStatus = inspectionChargeStatus;
	}

	public Integer getRadiationChargeStatus() {
		return radiationChargeStatus;
	}

	public void setRadiationChargeStatus(Integer radiationChargeStatus) {
		this.radiationChargeStatus = radiationChargeStatus;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRecSurStatus() {
		return recSurStatus;
	}

	public void setRecSurStatus(Integer recSurStatus) {
		this.recSurStatus = recSurStatus;
	}

	public Integer getBlDocumentStatus() {
		return blDocumentStatus;
	}

	public void setBlDocumentStatus(Integer blDocumentStatus) {
		this.blDocumentStatus = blDocumentStatus;
	}

	public Date getRecSurDate() {
		return recSurDate;
	}

	public void setRecSurDate(Date recSurDate) {
		this.recSurDate = recSurDate;
	}

	public Date getBlDocReceivedDate() {
		return blDocReceivedDate;
	}

	public void setBlDocReceivedDate(Date blDocReceivedDate) {
		this.blDocReceivedDate = blDocReceivedDate;
	}

	public Date getBlDocIssuedDate() {
		return blDocIssuedDate;
	}

	public void setBlDocIssuedDate(Date blDocIssuedDate) {
		this.blDocIssuedDate = blDocIssuedDate;
	}

	public Date getBlDocDispatchedDate() {
		return blDocDispatchedDate;
	}

	public void setBlDocDispatchedDate(Date blDocDispatchedDate) {
		this.blDocDispatchedDate = blDocDispatchedDate;
	}

	public Date getBlDocSurrenderedDate() {
		return blDocSurrenderedDate;
	}

	public void setBlDocSurrenderedDate(Date blDocSurrenderedDate) {
		this.blDocSurrenderedDate = blDocSurrenderedDate;
	}

	public String getYardId() {
		return yardId;
	}

	public void setYardId(String yardId) {
		this.yardId = yardId;
	}

	public Double getM3() {
		return m3;
	}

	public void setM3(Double m3) {
		this.m3 = m3;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}
	public void calcM3() {
		m3 = AppUtil.ifNull(length, 0.0) * AppUtil.ifNull(width, 0.0) * AppUtil.ifNull(height, 0.0);
		m3 = m3 / 1000000;
	}
}
