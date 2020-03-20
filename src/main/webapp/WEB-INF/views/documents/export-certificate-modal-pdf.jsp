<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="container-fluid">
	<table class="table table-bordered">
		<tr>
			<th colspan="1" style="width: 10px;">Serial No</th>
			<td colspan="1" style="width: 200px;"><input name="serialNo"
				type="text" class="form-control"></td>
			<th colspan="12" rowspan="2"><h1 class="modal-title"
					style="text-align: center;">Export Certificate</h1></th>
		</tr>
		<tr>

			<th colspan="1" style="width: 10px;">Reference No</th>
			<td colspan="1" style="width: 200px;"><input name="referenceNo"
				type="text" class="form-control"></td>

		</tr>
	</table>
	<input type="hidden" name="stockNo" class="stockNo" value="" />
	<table class="table table-bordered" style="width: 100%;">
		<tr>
			<th colspan="6">Registration No</th>
			<th colspan="2">Registration Date</th>
			<th colspan="2">First Registration Date</th>
			<th colspan="2">Maker's serial number</th>
		</tr>
		<tr>
			<!-- Registration No partition start -->
			<td colspan="2"><select name="registrationNo1"
				id="registrationNo1" class="form-control select2-select"
				style="width: 100%;" data-placeholder="Select Registration">
					<option value=""></option>
			</select></td>
			<td colspan="1"><input name="registrationNo2" type="text"
				class="form-control"></td>
			<td colspan="1"><input name="registrationNo3" type="text"
				class="form-control"></td>
			<td colspan="2"><input name="registrationNo4" type="text"
				class="form-control"></td>
			<!-- Registration No partition start -->
			<td colspan="2"><input type="text" name="registrationDate"
				id="registrationDate" class="form-control datepicker"></td>
			<td colspan="2"><input type="text" name="sFirstRegDate"
				id="firstRegDate" class="form-control"></td>
			<td colspan="2"><input type="text" name="makerSerialNo"
				class="form-control"></td>
		</tr>
		<tr>
			<th colspan="6">Trademark of the maker of the vehicle</th>
			<th colspan="3">Model Type</th>
			<th colspan="3">Engine Model</th>

		</tr>
		<tr>
			<td colspan="6"><input type="text" name="trademarkVehicle"
				class="form-control"></td>
			<td colspan="3"><input type="text" name="modelType"
				class="form-control"></td>
			<td colspan="3"><input type="text" name="engineModel"
				class="form-control"></td>

		</tr>
		<tr>
			<th colspan="3">Name Of Owner</th>
			<td colspan="9"><select name="nameOfOwner" id="nameOfOwner"
				class="form-control" data-placeholder="Select Owner"
				style="width: 100%;">
					<option value="AA JAPAN">AA JAPAN</option>
					<option value="SOMO JAPAN(PVT)LTD">SOMO JAPAN(PVT)LTD</option>
			</select></td>
		</tr>
		<tr>
			<th colspan="3">Address Of Owner</th>
			<td colspan="9"><input name="addressOfOwner" type="text"
				class="form-control"></td>
		</tr>
		<tr>
			<th colspan="3">Name of User</th>
			<td colspan="9"><input name="nameOfUser" type="text"
				class="form-control"></td>
		</tr>
		<tr>
			<th colspan="3">Address of User</th>
			<td colspan="9"><input name="addressOfUser" type="text"
				class="form-control"></td>
		</tr>
		<!-- <tr>
		<th colspan="3">Locality Of Principal Mode of Use</th>
		<td colspan="9"><input name="localityOfPrincipalOfUse"
			type="text" class="form-control"></td>
	</tr> -->
		<tr>
			<th colspan="2">Classification of vechicle</th>
			<th colspan="2">Use</th>
			<th colspan="2">Purpose</th>
			<th colspan="2">Type of Body</th>
			<th colspan="1">Fixed Number</th>
			<th colspan="1">Maxim. Carry</th>
			<th colspan="1">Weight</th>
			<th colspan="1">Gross Weight</th>

		</tr>
		<tr>
			<td colspan="2"><select name="classificationOfVehicle"
				id="classificationOfVehicle" class="form-control"
				data-placeholder="Select Classification" style="width: 100%;"><option
						value="">Select Classification</option>
					<option value="SMALL">SMALL</option>
					<option value="ORDINARY">ORDINARY</option></select></td>
			<td colspan="2"><select name="use" id="use" class="form-control"
				data-placeholder="Select Use" style="width: 100%;"><option
						value="">Select Use</option>
					<option value="GOODS">GOODS</option>
					<option value="MOTOR BUS">MOTOR BUS</option>
					<option value="PARTICULAR">PARTICULAR</option>
					<option value="PASSENGER">PASSENGER</option></select></td>
			<td colspan="2"><select name="purpose" id="purpose"
				class="form-control" data-placeholder="Select Purpose"
				style="width: 100%;"><option value="">Select
						Purpose</option>
					<option value="COMMERCIAL">COMMERCIAL</option>
					<option value="PRIVATE">PRIVATE</option></select></td>
			<td colspan="2"><input type="text" name="typeOfBody"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="fixedNumber"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="maximCarry"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="weight"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="grossWeight"
				class="form-control"></td>
		</tr>
		<tr>
			<th colspan="1">Engine Capacity</th>
			<th colspan="2">Unit</th>
			<th colspan="1">Classification of Fuel</th>
			<th colspan="1">Specification No</th>
			<th colspan="1">Classification No</th>
			<th colspan="1">Length</th>
			<th colspan="1">Width</th>
			<th colspan="1">Height</th>
			<th colspan="1">FF Weight</th>
			<th colspan="1">FR Weight</th>
			<th colspan="1">RF Weight</th>
			<th colspan="1">RR Weight</th>

		</tr>
		<tr>
			<td colspan="1"><input type="text" name="engineCapacity"
				class="form-control"></td>
			<td colspan="2"><select name="unit" id="unit"
				class="form-control" data-placeholder="Select Unit"><option
						value="">Select Unit</option>
					<option value="L">L(litre)</option>
					<option value="KW">KW</option></select></td>
			<td colspan="1"><input type="text" name="fuel"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="specificationNo"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="classificationNo"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="length"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="width"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="height"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="ffWeight"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="frWeight"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="rfWeight"
				class="form-control"></td>
			<td colspan="1"><input type="text" name="rrWeight"
				class="form-control"></td>
		</tr>
		<tr>
			<th colspan="3">Export Schedule Date</th>
			<td colspan="3"><input type="text" name="exportScheduleDate"
				id="exportScheduleDate" class="form-control"></td>
			<td colspan="6"></td>
		</tr>
		<tr>
			<th colspan="12" style="text-align: left">Remark</th>
		</tr>
		<tr>
			<th colspan="12"><textarea name="remark" class="form-control"></textarea>
			</th>
		</tr>
		<tr>
			<th colspan="2" style="text-align: left">Converted Date</th>
			<td colspan="3"><input name="convertedDate" id="convertedDate"
				type="text" class="form-control"></td>
		</tr>


	</table>
</div>
