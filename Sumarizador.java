import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
public class Sumarizador
{
	public Sumarizador(String[] argumentos)
	{
		List<int[]> listaTemporal = new ArrayList<int[]>();
		int iterador = 0;
		
		while(iterador < argumentos.length)
		{
			int[] temp = array_string_a_int(argumentos[iterador].split("\\."));
			if(temp.length < 4)
			{
				System.out.print("Error: Introduce las rutas en formato A.B.C.D");
				return;
			}
			listaTemporal.add(temp);
			iterador++;
		}
		
		if(listaTemporal.size() > 1) calcular_sub(listaTemporal,(byte)32);
		else
		{
			System.out.print("Error: Introduce al menos 2 rutas para sumarizar");
		}
	}
	 private static String aString(BitSet bs) {
		 
		if(bs.length() > 0)
		{
			String resultado = Long.toString(bs.toLongArray()[0], 2);
			byte restantes = (byte) (8 - resultado.length());
			while(restantes > 0)
			{
				resultado = "0" + resultado;
				restantes--;
			}
			return resultado;
		}
		else
		{
			return "00000000";
		}

    }
	public static int[] array_string_a_int(String[] a_convertir)
	{
		int[] devolver = new int[a_convertir.length];
		int i = 0;
		while(i < a_convertir.length)
		{
			devolver[i] = Integer.parseInt(a_convertir[i]);
			i++;
		}
		
		return devolver;
	}
	public static String[] calcular_sub(List<int[]> nets, byte mask)
	{
		List<String> direcciones = new ArrayList<String>();
		String primera = "";
		
		for(int[] direccion : nets)
		{
			String direccion_string = "";
			for(int octeto : direccion)
			{
				BitSet temp_octeto_binario = BitSet.valueOf(new long[]{octeto});
				String temp_octeto_string = aString(temp_octeto_binario);
				direccion_string +=temp_octeto_string;
			}			
			direcciones.add(direccion_string);
		}		
		
		primera = direcciones.get(0);
		byte lastmatch = mask;
		
		for(String direccion : direcciones)
		{
			System.out.println("Set de bits a sumarizar : " + direccion);
			boolean match = false;
			byte corte = mask;
			while(!match)
			{
				if(direccion.substring(0,corte).compareTo(primera.substring(0,corte)) == 0)
				{
					match = true;
					lastmatch = corte;
				}
				else
				{
					corte--;
				}
			}
		}
		
		resultado(lastmatch,primera);		
		return null;
	}
	private static BitSet deString(final String s) {
        return BitSet.valueOf(new long[] { Long.parseLong(s, 2) });
    }
	public static void resultado(byte lastmatch,String primera)
	{
		String resultado = "";
		int diferencia = 32 - ( lastmatch );
		while(diferencia > 0)
		{
			resultado += "0";
			diferencia--;
		}
		
		resultado = primera.substring(0,lastmatch) + resultado;
				
		int partes = 0;
		String ip = "";
		while(partes < 32)
		{
			BitSet octeto = deString(resultado.substring(partes, partes +8));
			long[] test = octeto.toLongArray();
			if(test.length > 0)
			{
				ip += Long.toString(test[0]) + ".";
			}
			else
			{
				ip += "0.";
			}
			partes +=8;
		}
		
		System.out.println("Summary: " + ip.substring(0, ip.length() -1) + " / " + resultado);
		System.out.println("Netmask: " + lastmatch);
	}
}