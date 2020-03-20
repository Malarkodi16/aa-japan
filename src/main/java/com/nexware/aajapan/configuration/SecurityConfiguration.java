package com.nexware.aajapan.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nexware.aajapan.services.impl.AuthSuccessHandler;
import com.nexware.aajapan.services.impl.AuthendicationDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	AuthSuccessHandler authSuccessHandler;
	@Autowired
	CustomWebSecurityExpressionHandler customWebSecurityExpressionHandler;
	@Autowired
	AuthendicationDetailsService authendicationDetailsService;
	private static final String[] SHIPPING_CONTROLLERS = { "/stock/purchased", "/stock/purchase-confirmed",
			"/transport/list", "/inspection/inspection", "/shipping/arrangement", "/shipping/status",
			"/stock/stock-search", "/stock/re-auction/list", "/shipping/shipmentschedule", "/shipping/schedule/list",
			"/user/create", "/a/supplier/create", "/a/supplier/list", "/a/supplier/create", "/a/supplier/list",
			"/master/maker", "/master/list", "/shipping/bl/document-draft", "/shipping/bl/document-original",
			"/shipping/last-lap-vehicles", "/transport/transporter/fee/create", "/transport/transporter/fee/list",
			"/master/location/list", "/master/location/create", "/master/auctionGradeExterior/list",
			"/master/auctionGradeInterior/list", "/master/hsCode/list", "/master/shippingCompany/list",
			"/master/ship/list" };
	private static final String[] SALES_CONTROLLERS = { "/inquiry/listview", "/sales/proformainvoice",
			"/sales/reserved", "/sales/shippinginstruction", "/sales/sales-order-invoice-list", "/sales/status",
			"/sales/stock/stock-search", "/customer/list", "/customer/create", "/sales/customer-transaction",
			"/invoice/porforma-invoice-management", "/invoice/sales-invoice-management", "/sales/view-shipment",
			"/sales/tt-allocation", "/sales/own-tt-allocation", "/sales/sales-home",
			"/accounts/specialuser/stock/data" };
	private static final String[] ACCOUNT_CONTROLLERS = { "/accounts/dash-board/view",
			"/accounts/invoice/approval/auction", "/accounts/invoice/approval/transport",
			"/accounts/invoice/approval/shipping/roro", "/accounts/invoice/approval/shipping/container",
			"/accounts/invoice/approval/genaralExpenses", "/accounts/invoice/approval/storageAndPhotos",
			"/accounts/lc/create", "/accounts/lc/list", "/accounts/invoice/booking/auction",
			"/accounts/invoice/booking/transport", "/accounts/invoice/booking/shipping/roro",
			"/accounts/invoice/booking/shipping/container", "/accounts/invoice/booking/genaralExpenses",
			"/accounts/invoice/booking/storageAndPhotos", "/accounts/invoice/approval/auction",
			"/accounts/invoice/approval/transport", "/accounts/invoice/approval/shipping/roro",
			"/accounts/invoice/approval/shipping/container", "/accounts/invoice/approval/genaralExpenses",
			"/accounts/invoice/approval/storageAndPhotos", "/accounts/payment/invoice/reauction",
			"/accounts/approve-payment", "/accounts/auction-payment-done", "/accounts/auction-payment-freezed",
			"/accounts/payment/tracking", "/accounts/payment/advance", "/exchange/list-page", "/accounts/daybook-entry",
			"/accounts/daybook-list", "/daybook/approve", "/daybook/tt-allocation", "/accounts/report/journalEntry",
			"/accounts/forward/booking/view", "/accounts/customer/approve/list", "/accounts/coa-list",
			"/master/list-bank", "/master/foreign-bank-list", "/accounts/master/shipping/list",
			"/accounts/user/management", "/accounts/create-loan", "/accounts/loan-details", "/accounts/re-payment",
			"/accountsBL/page", "/accountsBL/cr-management/page", "/accounts/billoflanding/form-view",
			"/accounts/executegl/gl", "/accounts/create/branch-salesOrder", "/accounts/branch-salesOrder/list",
			"/accounts/report/stockSales", "/inventory/inventory", "/accounts/report/sales-summary",
			"/accounts/report/inventoryValueReport", "/accounts/report/income-by-customer-report",
			"/accounts/report/accountTransaction", "/accounts/report/trailBalance", "/accounts/report/gl-report",
			"/accounts/report/trailBalanceTransaction", "/accounts/report/bank-statement",
			"/accounts/report/balanceStatement", "/accounts/report/profitAndLoss", "/accounts/payment/payable-amount",
			"/accounts/payment/receivable-amount", "/accounts/report/supplierStatement", "/accounts/customer-accounts",
			"/accounts/claim/tax", "/accounts/claim/recycle", "/accounts/claim/cartax", "/accounts/claim/insurance",
			"/accounts/claim/radiation", "/accounts/report/ar-aging-summary", "/accounts/master/shippingMarks/list",
			"/accounts/master/shippingTerms/list" };
	private static final String[] DOCUMENTs_CONTROLLERS = { "/documents/tracking/not-received",
			"/documents/tracking/received", "/documents/tracking/export-certificates",
			"/documents/tracking/name-transfer", "/documents/tracking/cr-received",
			"/documents/tracking/export-cerficate-tracking", "/documents/recycle/claim",
			"/documents/recycle/claim/insurance", "/documents/year-of-manufacture/view",
			"/documents/year-of-manufacture/create", };

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authendicationDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().expressionHandler(customWebSecurityExpressionHandler).antMatchers("/").permitAll()
				.antMatchers("/login").permitAll().antMatchers("/error/*").permitAll().antMatchers("/fbfeeds")
				.permitAll().antMatchers(SALES_CONTROLLERS).hasAnyRole("SALES", "ADMIN")
				.antMatchers(SHIPPING_CONTROLLERS).hasAnyRole("SHIPPING", "ADMIN").antMatchers(ACCOUNT_CONTROLLERS)
				.hasAnyRole("ACCOUNTS", "ADMIN").antMatchers(DOCUMENTs_CONTROLLERS).hasAnyRole("DOCUMENTS", "ADMIN")
				.anyRequest().authenticated().and().csrf().disable().formLogin().loginPage("/login")
				.failureUrl("/login?error=true").successHandler(authSuccessHandler).usernameParameter("username")
				.passwordParameter("password").and().logout().invalidateHttpSession(true)
				.deleteCookies("remember-me", "SESSION").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout=success").and().exceptionHandling().accessDeniedPage("/access-denied")
				.and().exceptionHandling().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

		http.headers().frameOptions().sameOrigin();
		http.headers().defaultsDisabled().cacheControl();
		http.headers().xssProtection().block(false);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
