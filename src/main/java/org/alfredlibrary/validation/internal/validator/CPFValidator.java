/*
 * Alfred Library.
 * Copyright (C) 2011 Alfred Team
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.alfredlibrary.validation.internal.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.alfredlibrary.validation.annotation.brazil.CPF;
/**
 * Validator based on JSR 303 - Beans Validation.
 * 
 * @author Marlon Silva Carvalho
 * @since 2.0.0
 */
public class CPFValidator implements ConstraintValidator<CPF, String>{

	@Override
	public void initialize(CPF constraintAnnotation) {		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		value = Pattern.compile("[^0-9]").matcher(value).replaceAll("");  
		if (value.length() != 11)
			return false;
		String numDig = value.substring(0, 9);
		return gerarDigitoVerificador(numDig).equals(value.substring(9, 11));
	}
	
	private String gerarDigitoVerificador(String value){
		return obterDV(value, false, 2);
	}
	
	
	private String obterDV(String fonte, boolean dezPorX,
			int quantidadeDigitos) {
		if (quantidadeDigitos > 1) {
			String parcial = obterDV(fonte, dezPorX);
			return parcial
					+ obterDV(fonte + parcial, dezPorX, --quantidadeDigitos);
		} else {
			return obterDV(fonte, dezPorX);
		}
	}
	
	private String obterDV(String fonte, boolean dezPorX) {
		//validar fonte
		int peso = fonte.length() + 1;
		int dv = 0;
		for (int i = 0; i < fonte.length(); i++) {
			dv += Integer.parseInt(fonte.substring(i, i + 1)) * peso--;
		}
		dv = dv % 11;
		if (dv > 1) {
			return String.valueOf(11 - dv);
		} else if (dv == 1 && dezPorX) {
			return "X";
		}
		return "0";
	}
	

}
