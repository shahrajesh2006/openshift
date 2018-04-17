package com.loyalty.saml.test.utils;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.xml.security.encryption.XMLCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class CommerceBankSamlTestHelper {
	
	final static Logger log = LoggerFactory.getLogger(CommerceBankSamlTestHelper.class);

	private static String RANDOM_STRING = "abcdefghijklmnopqrstuvwxyz1234567890";

	public static void main(String[] args)
	{
		String token = "PHNhbWxwOlJlc3BvbnNlIHhtbG5zOnNhbWxwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiIElEPSJsdXkwbHhzcnJ3emE4dzZ5bmZkY3k2bXJocnI2amxsN2JvcWQyMHB5bWwiIFZlcnNpb249IjIuMCIgSXNzdWVJbnN0YW50PSIyMDExLTEwLTE3VDE0OjI1OjMzWiIgRGVzdGluYXRpb249Imh0dHBzOi8vc3NvLnRlc3QuYWZmaW5pb25ncm91cC5jb206NDQzL29wZW5zc28vQ29uc3VtZXIvbWV0YUFsaWFzL3dlbGxzZmFyZ29fcmVhbG0vQUxHX09NQV8xMCI%2BDQoJPHNhbWw6SXNzdWVyIHhtbG5zOnNhbWw9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPkFMR19JUF8xMDwvc2FtbDpJc3N1ZXI%2BDQoJPHNhbWxwOlN0YXR1cyB4bWxuczpzYW1scD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj4NCgkJPHNhbWxwOlN0YXR1c0NvZGUgIHhtbG5zOnNhbWxwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiIFZhbHVlPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiPg0KCQk8L3NhbWxwOlN0YXR1c0NvZGU%2BDQoJPC9zYW1scDpTdGF0dXM%2BDQoJDQo8L3NhbWxwOlJlc3BvbnNlPg0KCQ%3D%3D";
		log.debug("Token = " + token + "\n");
		KeyStoreHelper ksh = new KeyStoreHelper();
		Key key = ksh.getPrivateKey("projtest");
		String decode = decryptSamlToken(token, key);
		log.debug("Decoded Token = " + decode + "\n");
	}


	public static String decryptSamlToken(String token)
	{
		// String token = "PHNhbWxwOlJlc3BvbnNlIHhtbG5zOnNhbWxwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6%0D%0AcHJvdG9jb2wiIElEPSJzMmMyMTllOTYzYTc5M2RjMGY1ZjI1MWJlN2MyOTlhZWViMjZiZDFlOWYi%0D%0AIFZlcnNpb249IjIuMCIgSXNzdWVJbnN0YW50PSIyMDEwLTAzLTA0VDE3OjQ4OjEyWiIgRGVzdGlu%0D%0AYXRpb249Imh0dHBzOi8vc3NvLnRlc3QuYWZmaW5pb25ncm91cC5jb206NDQzL29wZW5zc28vQ29u%0D%0Ac3VtZXIvbWV0YUFsaWFzL3dlbGxzZmFyZ29fcmVhbG0vQUxHX09NQV8xMCI%2BPHNhbWw6SXNzdWVy%0D%0AIHhtbG5zOnNhbWw9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPldlbGxz%0D%0AX3FhMTBfaWRwPC9zYW1sOklzc3Vlcj48c2FtbHA6U3RhdHVzIHhtbG5zOnNhbWxwPSJ1cm46b2Fz%0D%0AaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiPgo8c2FtbHA6U3RhdHVzQ29kZSAgeG1sbnM6%0D%0Ac2FtbHA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCIKVmFsdWU9InVybjpv%0D%0AYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyI%2BCjwvc2FtbHA6U3RhdHVzQ29k%0D%0AZT4KPC9zYW1scDpTdGF0dXM%2BPHNhbWw6RW5jcnlwdGVkQXNzZXJ0aW9uIHhtbG5zOnNhbWw9InVy%0D%0AbjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPjx4ZW5jOkVuY3J5cHRlZERhdGEg%0D%0AeG1sbnM6eGVuYz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjIiBUeXBlPSJodHRw%0D%0AOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNFbGVtZW50Ij48eGVuYzpFbmNyeXB0aW9uTWV0%0D%0AaG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjYWVzMjU2LWNi%0D%0AYyIvPjxkczpLZXlJbmZvIHhtbG5zOmRzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRz%0D%0AaWcjIj48eGVuYzpFbmNyeXB0ZWRLZXk%2BPHhlbmM6RW5jcnlwdGlvbk1ldGhvZCBBbGdvcml0aG09%0D%0AImh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3JzYS0xXzUiLz48eGVuYzpDaXBoZXJE%0D%0AYXRhPjx4ZW5jOkNpcGhlclZhbHVlPkQ2c2JXSHFNL1oramJ3WjIwckQ4THoxUFQwUmlvUkEzaldE%0D%0AUExqZEUzRWswb3FpQTNqSm1sbHhkZkdMdlhpUmpacjQweXMzZG53MHoKOWRBSUxkQjRWdmFTM1dM%0D%0ASUxMaHROM1NZczQ1OUkzLzFOZzdSQXR4MThnWjJBUlJGUUFsSGtLYXZsQjhlc0FGaXo3b3FEbzYv%0D%0AMU5XQQpONlBYWWxQbHEvenpaVE9VSzVtYXB5aUFXRElhQWhlUk40UlFUYURuZklybTlsdG5BVUFU%0D%0AWDdoNXlHYVUzR3o0bEpEQXhJK205elZnClpDQnBtRWlBbWFKb1gvR3ZGTkNQeE5uUmZBZkh3K0tj%0D%0AZzBMUGpIU1Yxa0VaK1dWcU44R2x5bkh1REs0Y3Y3NFdjQVMvVVl2ZVQ0VDAKVnVHcWZzUVVRU0Ix%0D%0AUWUrcHVVdkhZdkFpVmJUWUl5ckdJTUc1S3c9PTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lw%0D%0AaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkS2V5PjwvZHM6S2V5SW5mbz48eGVuYzpDaXBoZXJEYXRh%0D%0APjx4ZW5jOkNpcGhlclZhbHVlPjNtejNQdEc1MGNyVlBPMUR1alFTeDh5cVB0QUs0cllLZXBwTVpi%0D%0ASEdOS09KUnBBTTY4ZGRtWEV1Y2k0QVo5OU1BQ0t3K0NkYWFkbEoKS2lhL0JIeXdhU1YwM1hWQndB%0D%0AREFMZFFrQ2hqVmRBZ2lDZCtWYmZ4NW9qSmFvTW1YOG9TandjN3N4ZWQrTHZkZlpUeHBBSStFeEYv%0D%0AYwpwekM4OVc1WUZkcmxKTmhmc3FQb1FIYWg1ekdJdVlEdEhRS1EzSVN2eHFyR2cwWUpBcDU5WG1m%0D%0AU0tJN091c01WSjlBbVE3MGc4S2hwCmVsTDV2dnlkSERncjVZUlBQY1ByK1dzcjd4cmtRRlc4MC9C%0D%0AaHdNeW5aK0FXcXFySWU5TFBZV2xNZmVJZDFjZXlnM0JyQmZ4MmFnWkQKOXM1cm9taWZXVU5yVkQ5%0D%0AaDRQRzZaZWxQcjFpaXp2R1kvTkJUeThlTzQvQlJ6MWVSQVRBM0tQaDNxQ09Yd3VNWkJMZGFqVHl6%0D%0AV3VVQwpyT3plblVsTzJJZ1Jkc2UrV21sdHVQdW9SZ3RWbHdpT0ZHMncwRS9Rc0FOL2RWdGZIODF5%0D%0AbFFpU2t4dGlPTXE3V2kzTVRJaFdHeGdKCkRvSmh0djlSRTdpaW8wWWswVFlsRmx4QVJDTU1OTHBX%0D%0AdUM1dEFLNENuaDdmaDJoQWxTMEhqTnZGNU56UVk5a0c4aVUzY2pvTXlzNU0KY0gxczhVeGxsUnNz%0D%0AQ25IMUdCRHc2elozWUdiQURURmFHWTRDQUxrVVlLaHVicFFISVlaU1NuWk1YbHlTSFQwbXFENzM3%0D%0ANXpBMVF5TApEekcwZklzWWFxdGM4a3p0bnNZZUJUTEdRVWdFMVlKSHo4bWhSczQ4bHdOL2JHeU5z%0D%0AQ1BOY0w0aGM1aXVHM3JTbklYUDdLWVJNNFU1CktVaEg3cmtXWWdpNCtXdHZDUExsbDhRbFJEZVNM%0D%0AZ3VzSndsUFZjZ05td04vRWdyakF4aC9LWElYSEFtakh4WFM5K01zc0VPUnhhMEcKT1lzRDVtQ0JQ%0D%0AVThoekJFc2RpbGxMRFFhODEyRWYxRm0rKzVPZWR6a25IUGo2MUI4Q21GZlYzWmVZd01ydk9XZnd5%0D%0AZi8yb0hRakk2OQptTGxERnVETzZtSUlaR1BlR082Skd2ZFJnU1V3TzVEL2VBeHpaaCtQdkt0b3JR%0D%0AMmtYcGJaZXIrQTBid1lYTHE1bjAzbmEveXhaSUZsCmpIL0thZ1Z2QWdVRjhtd1NoU2FENkx0WXRZ%0D%0Ad0RGZXpvN2hudkhvUUJ4cGlCdlMvZGJ6c1l2OUJYQ3NyazJGYUZHY05sbHZkZXQ5QXIKWEkvMS9l%0D%0AdkxRck0wblh1MjhxNXVVdDQzK1Y0c25TUXRpYzEveXluWUJrbWYvMDNoWVpNalp5S2ZoMFFMekpO%0D%0AMkIxLzI2Mm1zeGticwpTKzQzaHJ2UFh0elNRN0dlZHJXd1JEWmdiUCtpNmNiZmVJdmFYTVVyV3Ri%0D%0AbG4wUTJQYUxQdC9lNEgzcWlsNlRsSlJGT1VrTm9UYUVjCkEwR2wzMHhycUZpaGJUWUNuaVNYZU9q%0D%0Aa1VJRHlXNmtvdWc3SGYvQnBiWUwrdCtJd3BLeVZKRm1TbUI0OGFUWXNDUERISWpCWGRzdGEKNGpp%0D%0AamF5STBuT0t5RDZDalNmSEJDa2p4NlhKVWJqeXdHQW5STDlnWXh1REdqMmVHVTYrcTZ2cm1IOFJa%0D%0ATUtPSUFQWmhyVEFSWFVNSQp1V3ZCTDlvTDNCelpwODJoTkdTZ29rTUJwRmVKaFFYWHZDcUIxSGZZ%0D%0AQ2pzZDRmWldJbnQ1c250TkpXQ0MxVDMvQTE3dFdHOWx3Y1g3CnpWWlk5Rk01SzRuY3I4QXRQQlFz%0D%0ANUFucUNHcmEvT2x1ZVpPeWFDUjVabXAwY1V5YnJPL1lFK1FTN1JZQ2tjV3Vvb25QQlVsdURUZEwK%0D%0AZzlTS1dFY1dOdmpXY0lTOTRQKzJLSlBQeU1FV01BdnlRelh1cXBpTGthbUs4UEI4NzZIZ2xVcmpN%0D%0AYm9JbFRVRFZ1N0hEZklwaDZmMwp2OWpVTTJObEFuZnBFN3VQVUhlbFB2bjBhdzZoRnlCenBwcUhK%0D%0AemRYSllTL0l6ZHZEaVprRURVL012U1lqbGw0LzJJUVdPcUUyWG51ClhnT1pBbzk3NytFNkpoREhT%0D%0AUlBIQ01LMWREUGxXYkxBZ1BnVytEUEhnNnRteXFsUDdnOTBBUXJ0bHE2RjFReEZhTllzcVIrLzRQ%0D%0Ad0IKa3dQajhPa1FIVFlXbUFzUnlBQ2N1dGNyczJwa2NJWG9rUkJuUHhJT0RUTWRwRng5T1RTN0pD%0D%0AZXVSTm0rNFZqdGVmOEYwMUdHeHhFRgpQbzlPZE5namhFMlFVWnV2cGQyem5FMWdmSVNvOUpYUlJD%0D%0AbXlxczgxUHBpaEVCQk9rb3BFN3ZNdGo1NGg1bUdPL2NKbEZ0MEY2YjFICmVrV0NJY2RWSDFLVlU3%0D%0AcVpkNnJzNEZseEVZTFdwVk1ONlNnZGhGdDNqdDZDRUp2dThDS2xhTS9ZYm95OGM1TzMzWGlXQkpa%0D%0AQjJOYjMKZWd1NVZ2S1lxOHREMTRIVnlxSWw3ZWVUemRKbXNJR2c3M2NWQjB4dmFxZ1lWdzBFQk1a%0D%0AK3JxY0d0TUZxK3E2ZW1scWorZjZiSHhwOQp4Zy9abWZ6cUVaYTN4eFh1d2UxcGpXL3JXaDRPUE5H%0D%0ASkZGRnA4Tm5BMzZWaFFRU211U0NmWTJhRFhVVHAyWW52Y0dveU50dXU5RTE0Ci9Gb3c3WlErWUY0%0D%0AR0FZTlNmMXNVYW9GNWVXT1ZISHBDZDFMeU8yTEpLY0t2ZzNSVTlyUUp4ZWtxMGhlWWoxOFNlaFJ0%0D%0AS0paVkY4MFkKcEdGeHA5MjMzZGtDcVRCQlMxZzE5d05hYUUyNGIxUTZqek1NZTFWcGxMVE1iTzMx%0D%0AV1ZrQW4xZUZFYkdzZW50MGFFR2dMZU5kK2k3QwpZYTEvZ2hOLzhlTExSQ3NvNWtNUW96OWd1bWxz%0D%0AVUI4ZVNualMwUXJGeHRlU2ptblNScGpDMlBVUVBTR3hWMjVlR2FaclBNU3FxQjhxCjMxcHhCdEtm%0D%0AbUZ1QWVFbDZZZUpVNEhmelNWK0twOVU0bkVmZjBtMms3MHBGOXhxMXJXZGFiRU1Pc1d0NldYN1Bh%0D%0AWkRKWlNFNHVFM2oKNWs1ODFZZ2lNU0tOYUV2a2pUTXQzNld3VDNHRU9JWk5CdUFlMjNTZDZSdE1H%0D%0AN2FnTUU1bGRaVklzWjlzUE55US9xaVFXWERjdkV1WApPMFU4MzBOY3JJUFJDbnZKT29DU205NHhy%0D%0ANkQrSGEzcUZVMnhkME54Zno5L0lza0lQY0ZlQk1wcUdjTFdNKzl0emFHZm04c2pPZ2prCld1ajMz%0D%0AN0hvZndmZ0toVjdWMUhZb09wY1hKK0pTL3FydGpIWCt3eGtET1RXNzRNTUhmTHRFNWFLQ0hPa2U0%0D%0AQmVUMGduMjUxQWZ1b0YKZTlZUEhzUGJYR3RjZXl3WlpjZXFqWmxLemRUVEh2M0pMczJxUkd1WWRI%0D%0AdWVOaEk3eE1xM05tNXZCSEVyWDB6K1NjMWwxNGEzM2NYdwp4YkNvdi9EcklEZDJxM0krWjNCNmgy%0D%0ASEM1N0ZDNktJUnBzbmk2emZPTU9yVjZuV1V6c0I5MTNFanJ6MlRtbE51REQzcXVqV0xRek1VCmdy%0D%0AcGpMRkljdDc1QUUrU1BpdEpLaUg4cmZFSlBNQW54ejlhcFhBbUszOXo5YmdXWjJyMXFQdDFwL3E3%0D%0AN3NpVkh1SVkvNG9UMVlaR2EKeVNyVWdDRnUwT21vVXVFTys3TVVnY3g2Tkxscm5PeitFemdkK0M0%0D%0Aa0lFdjJqci9tSU9DNUNENjU0U1pOUkc3OVNkSG8zSGFRRE9rUwpabGljV0MwdElIcVN1VXVzS0ky%0D%0AMm51UlhFU1ozMVpKV09qczZWYVR3QTJkVjM5MVlFK0o5c0ZuNHpMdmJDVHhGcUNid0V6NlQzQm9r%0D%0ACjdZblZReE8wSVZRRldld0tOY1BFSU9YTlZodDlVR0ROenY5UVRTU1JIeDhLUjBPYmdacjZUUjM5%0D%0AN1MyKzRYeGdCemtic1R4T1ZlbW0KZkV6azM3aFVYNWJnSDQwM1NtSE8wVm02WU9mTUI1eHhncmxp%0D%0AcG5hOS9KU2pRd0ZVS3BCdTJ4V3M2NXFzTXU4WC93bGRyNUgwbm9kcwpDVUwreEp0YlJVNG0vVXdK%0D%0AdEtWZEtPUzRTekp3dDVGTExjMFZYTjV6a0NJc1pVMjFBVFJsNmlQZVRvZkhYb294a0RXaWNGK3c0%0D%0ATlhTCjB5cUt4QTdmeUhpejc2VWhnZXBjaUlRNlduN01JTngvbzVQalRuQXNQK1pLenZnZVlvM29i%0D%0ATWN4TmJDbnpSRklFeGhzZlh6ZTlSaGUKc2Jpb2piUDFEa1I0aUFUYWJkZVdIMHJEREdERHB1bnFO%0D%0AM1Z3anoveTJXdTJpQlhaZ3hienpqaUdWNHNZK2pIWWVSSUZ0V2xQelVBUwoySVdoUzl4dHhPdzcy%0D%0AUlI1c2hNTk9RUm9RcXJEdkNsMU12ZkFWSEY1N3BscjRjUnRQOVNHWGlTbjB3TSs1TGRoMlQwc0xI%0D%0AblRZSk1MCkhObmxZNi9SMndXUVMyaTFLdHdONUFJYlBMUUlnQUtyNXY4bGt3WWhlMng2ZXQzNXlX%0D%0AQmlPKzduVVNlY3pWRE9laW9OMGwrSlgrRW0KcGJnN0pzUUZjcnFydlI2aXR5OUNTUW9VWlUyT3VK%0D%0ATGdMak5nbUQwejljQXZuQktDNEY2bWduQ1N3ZVJndVByNnpEb1hqY29EU0ppNwpwN3h4L2tYWFpZ%0D%0AR21zRThUSWM1ekhuN2dLZDVhTWJYTGFHV3cyQThDSlRNa0ZvazI5NUJiREpXZ3JYb09FajlDdjVz%0D%0AK2hYVmUxcHFOCm1VUFVaeXRMdlNCczJEQ2EzRlBHOGV1MVdMclppL1JjTzE2MVlKMThMd1NqSHBJ%0D%0AbStadnQ2Nnp1QmJkLzYxNXhnbzlkUjNKeGV0T2MKWGF0ZDI1NTVXb0JRUk82QUFna1c4TEc0SDN3%0D%0Ab3pxVmtNd2RxeURJanZNNzVrNXFVbGdnNDhqeHloV3ZwMHI1UkNQekx0NElsUHZXcgpiSmV5MEJz%0D%0AZ1I4emFQQk5WY2M5VEhuTWhYeGQyYng1dU8yUkFENWdIMEU5MXNFRmN6QUxTZXRTQkR3empkUG5U%0D%0AWnFQZFFPUC9RZmJxCmV1OVBnR2xaSW9NK2kydWZwNis1aythR2Jvc05wZ1FMaWVzSFJ5QXhyWlht%0D%0AQnpYYThsRy9tcExtN290VFFZQlFhY1k1L1EzNEh2czcKeS9TTm4wNS9FVzdOVDIwYVFNbEljeFlU%0D%0AUy8wMVJmTitVQmRhckJSZVVqUlRRNUlMR3BEM1grRGkwaldvaFhMTFVwK1dlVUgwN1d3UApiL0xi%0D%0AOEFSWTBKMzdQUGVteUtVa2RUdEorblBuZkxSOUNnVWFaTGFIWkhPaGN4QU1VT1IzdklKVXQ5dDJo%0D%0Aa2NPUEFRamJWQnpkTndhCnZvMENCY091ZHVlSUdMSkFPNFNwRmN0THl2bUdhY2NTVTV5eHJ4UmZL%0D%0AVWpTZFVPbnVHaDVVODNIdkNIQnQ0NSs1ODBHNWxKcitSancKWG5JY3VObHkxdWl1MU8zRGV4dTVo%0D%0AOEhrZ3d2U3YzSTEzYnc5SXBLaVNsV2dtQUhHWE8xcTJYSCt0TytHT0U0SllFN0RReWM4bW5rYQpM%0D%0AZEswK1RFbHJ2MW14SzhmL21iTkluREN4aURmRXlJTEI1RjZ0NjZoZnlSMGJwYy9QSFdDQU1zejE1%0D%0AY0pIbFNKdVl6aUlGQ1JINXR6CkVRdVUwdElZMjFKNWlubG1saldBUUlKenowY1Q0dnRDbXl3ejlG%0D%0AWmN1TVdvcml1dGJMRHhxK1JYdmJyVlNFcW14TmcvMlRIZXBpMFQKNVBMSVFXQVZ2eTYzaERROEds%0D%0ATEJkZERNbGtKREMzM2JnTDI5ZHJXOGlQeHhUcFM5aVNzeGZWU3ZSMTJXb2ZZUDVpM2NmY1ZHS2w1%0D%0ATApGRERjWGt5aDRLRUJPVmdBL3pxeCtwd1VqTmhERXZEUlNjcnAwaVlCbVRMOVQrTzdLK2MxbEFO%0D%0ASVRZUkoyNkJ0cXhHaHJzNHNRRFVnCkNWSGllRUdiMG96T2xGa1VOblVYSERNNDRDTlprb1RRSXlJ%0D%0ANDVvdjR4MmhCajlLaWZoSk9vMlVYcDhVR1B6NUk3bDJrRjJmNVJ4U2IKaHFPSDBOdDJxajRYUzBn%0D%0AYTNYM0ZudDJOcG9YZktCWG84Uy81dERidjRGd3A2RXFNWTZaaVNxVXZKVXBvZHc3R2c3Wkl5T1lB%0D%0AWVZMdgovZml0QWtaTE43NXI3OW0xUTlLbHV6MkcvdHc0OSsxYzNMdnpvT0dvV2twVHNYSEthci9n%0D%0AbEJ2UjQyRjhxRXJsQ29iL1Zwc0hoNDBrCkN3c2VObDlCTStCU2xEOStTUGowbXB3M3JNZENBS1F5%0D%0AL0w4TUhCb1NweXFvNXdOVFVZOC9MRDhLRHNvcVdUb2VYN0I1cTdUb3Nhb3YKZ3c2WGRhcTRPYlRO%0D%0AdFlVOHA3NklDVHF3QnBIdk5yZWJBWTZ6OThQNmdXQWdVWGduSmtucnVGQkFqb1hwL2hQeHh5VW9D%0D%0AUW1TMlBwNQoxMUErdW12TE0xSXlMVUFHZVJObVR1Z2N5Q3V3Z0RyY3YzaitHUGR5SHZsbTd6NW0v%0D%0AN0czd2NYU28yUmZtejcyZkEvVUhDeFVmbXdmCnA3cXp2d0N6VHdZMTZBVEdlM1RaVnNOSXpLU1dk%0D%0AK1VRS1pSUjdSRWdYaXNFdVczenA1ZzVKQjNUc1IveWFmNFNubzczWWVMYU5jYVYKTHluZ29CYmNH%0D%0AZFZXL3E4K0hYbFlYbVlJSzUrczFUaXk5V0RoZko3WUxFUml6ekpSVWhMNHF4SEZmdHhYeWx4SjJo%0D%0ATlhJdGhTbVBRYgpxMlRubENPVWNLZjd6ZHJ5by9CUlg0TWsxdGpIK3lTYSs2UksyYnNtMzRVOVE3%0D%0AeERzSW5pTnVZZVRVeTlocy9wOGxyait1a1MxNXpBCnVjNWpMN1lVdGE1eldIUFlKa0J1Vnh6RWc2%0D%0ASGNGcVJKaXpyV2h3R0RKV3JtR0hucktseXR5aDRzUEt2RFhKTHp6c1JEVG1EWDd5QWUKb3l3SGxJ%0D%0AdUdVWjRYQnB4SXBNQTV3YmlqSGwrTGJZT3pZS0VncENleEtnSmVHN3VnZlM1TTZoUXZZUEozUVlP%0D%0Ab3ZYZGZoVEFaQnF3bwppcUViVzh4ZUJScGR1RzhpTG9hbTZJSnRPQnhUWWJJaVovbDJKYm9IeDJo%0D%0AWG5DSEF1OTYvWU9yQUsyY3lmd2I5QUgrS0dtMUI2UGFKCiszcm1BSEVsc0ZjZHlpYnBXaUw2bmdP%0D%0AOW9naUtKZ0FjWFFYU2I0OWtuQWJTOCtRdTZWTlZHUU0rTzg1QzkzNk5HOG93VFR5b0lIZHQKWGk2%0D%0Ad01Lc29sWkFjL0h4OTZBNGJPWWNVTjdkTG9JUnJDOGRYWTNoNnBaOTRrblh4N0x2aUJjR1ZQZVAv%0D%0AMXR0SzJDV2JBdE85dmVaUgp2eEJuSHdqQWQ3UDlVM2tkTTlXQzNPU0p5R0QwRnEvcWorY2pia1Yr%0D%0AUUd3L09EbXlKSnhNbndiWEYrTGxhS2dBa0FrbSsvbnN6NzI0CjdGZUdkTDJzaFA1M1FUUkxlUlJL%0D%0AdlVwcE5nUERSUmRhd0hFaXFmVm5yMzBPd3hPRU11S3lzYnk2TTBmZCtnU1EyMndVYXNJNXc5UUwK%0D%0AaEFFdVdqSkE4SlZZRHN1NWdPSDR2QXNUTThjPTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lw%0D%0AaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkRGF0YT48L3NhbWw6RW5jcnlwdGVkQXNzZXJ0aW9uPjwv%0D%0Ac2FtbHA6UmVzcG9uc2U%2B%0D%0A";
		log.debug("Token = " + token + "\n");
		KeyStoreHelper ksh = new KeyStoreHelper();
		Key key = ksh.getPrivateKey("projtest");
		String decode = decryptSamlToken(token, key);
		log.debug("Decoded Token = " + decode + "\n");
		return decode;
	}

	public static String decryptSamlToken(String token, Key privateKey){

		// URL Decode
		String urlDecode = URLEncDec.decode(token);
		log.debug("URL decoded Token = " + urlDecode + "\n");

		// Base64 Decode
		String base64Decode = new String(Base64.decode(urlDecode));
		log.debug("Base64 decoded Token = " + base64Decode + "\n");

		// Decrypt encrypted token
		String decrypt = "";
		XMLEncryptionProvider enc = new XMLEncryptionProvider();
		try {
			String encryptedAssertion = base64Decode.substring(base64Decode.indexOf("<saml:EncryptedAssertion"),base64Decode.indexOf("</samlp:Response>"));
			log.debug("Encrypted Assertion portion of SAML Response = " + encryptedAssertion + "\n");
			Element decryptXml = enc.decrypt(encryptedAssertion, privateKey);
			decrypt = XMLUtils.print(decryptXml);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.debug("Decrypted XML = " + decrypt + "\n");

		return decrypt;
	}

	public static String createSamlToken(String accountNumber,  String authURL, String encryptCertAlias)
	{
		//String accountNumber = "123456789CMSIG"; 
		log.debug("accountNumber = " + accountNumber + "\n"); 

		// Create a SAML assertion, inserting the payload
        Date now = new Date();
		String samlXml = generateSamlAssertion(accountNumber, now, authURL);
		log.debug("SAML XML = " + samlXml + "\n");

		// Sign the Assertion
		KeyStoreHelper ksh = new KeyStoreHelper();
		String signedSamlXml = signSamlXml(ksh, samlXml);
		log.debug("Signature = " + signedSamlXml + "\n");

		// Encrypt the Assertion
		Key key = ksh.getPublicKey(encryptCertAlias);
		String encrypt = encryptSamlAssertion(signedSamlXml, key);
		log.debug("Encrypted Assertion = " + encrypt + "\n");
	
		// Place the encrypted assertion into a SAML response
		String response = generateSamlResponse(encrypt, now, authURL);
		log.debug("Saml Response XML = " + response + "\n");

		// Base64 encode the SAML response
		String base64Encode = Base64.encode(response.getBytes());
		log.debug("Base64 encoded Token = " + base64Encode + "\n");

		// URL encode
		// String urlEncode = URLEncDec.encode(base64Encode);
		// log.debug("URL encoded Token = " + urlEncode + "\n");

		return base64Encode;
	}

		private static String generateSamlAssertion(String accountNumber, Date now, String authURL){

		Map<String,Object> map = new HashMap<String,Object>();

		// SAML data
		// payload
        map.put("accountNumber", accountNumber);
        // time information
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        map.put("timestamp", df.format(cal.getTime()));
        cal.add(Calendar.SECOND, -30);
        map.put("timestampMinusHalfMinute", df.format(cal.getTime()));
        cal.add(Calendar.SECOND, 75);
        map.put("timestampPlusOneMinute", df.format(cal.getTime()));
        // miscellaneous values
        map.put("assetionId", RandomStringUtils.random(42, RANDOM_STRING));
        map.put("name", RandomStringUtils.randomAlphanumeric(28));
        map.put("session", RandomStringUtils.random(42, RANDOM_STRING));
        map.put("authURL",authURL);

        // merge data into XML template
        String samlXml = VelocityHelper.processTemplate("velocity/samlAssertionCBKC.vm", map);

        return samlXml;
	}

	private static String signSamlXml(KeyStoreHelper ksh, String samlXml){

		String signature = "XML Signing FAILED";
		XMLSignatureProvider sigProv = new XMLSignatureProvider();
		sigProv.initialize(ksh);
		try {
			signature = sigProv.signXML(samlXml, "iputil20_idp.cxloyalty.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	public static String encryptSamlAssertion(String samlXml, Key key) {

		String encrypt = "";
		XMLEncryptionProvider enc = new XMLEncryptionProvider();
		try {
			Element encryptXml = enc.encrypt(samlXml, key, XMLCipher.AES_128, 128, "ALG_OMA10_SP", "EncryptedAssertion");
			encrypt = XMLUtils.print(encryptXml);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return encrypt;
	}

	private static String generateSamlResponse(String encrypted, Date now, String authURL){

		Map<String,Object> map = new HashMap<String,Object>();

		// SAML data
        // time information
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        map.put("timestamp", df.format(cal.getTime()));
         // miscellaneous values
        map.put("responseId", RandomStringUtils.random(42, RANDOM_STRING));
        map.put("encryptedAssertion", encrypted);
        map.put("authURL",authURL);

        // merge data into XML template
        String samlXml = VelocityHelper.processTemplate("velocity/samlResponseCBKC.vm", map);

        return samlXml;
	}

}
