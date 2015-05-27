package br.com.tagme.commons.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ImageDownloader {
	
	private static Map<String, ImageResource>	mappedImageResource  = new HashMap<String, ImageResource>();
	
	public static void registerImageResource(String resourceName, ImageResource resource){
		mappedImageResource.put(resourceName, resource);
	}
	
	@RequestMapping(value = "/image/{resource}/{imageId}.{extension}")
	public @ResponseBody String imageDownloader(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable("resource") String resource,
			@PathVariable("imageId") String imageId,
			@PathVariable("extension") String extension, 
			@RequestParam(value = "w", required = false) String w, // width
			@RequestParam(value= "h", required = false) String h, // height
			@RequestParam(value = "q", required = false) String q, // qualidade
			@RequestParam(value = "s", required = false) String s, // suavizar
			@RequestParam(value = "r", required = false) String r, // reduzir
			@RequestParam(value="z", required = false) String z  // Foca na area central da imagem dando um zoom. Esse valor deve ser definido em pixel.
		) throws IOException{
		
		double quality = 1.0;
		
		if (q != null) {
			try {
				quality = Double.parseDouble(q);
			} catch (NumberFormatException ignored) {
			}
		}
		
		response.setContentType("image/" + extension);
		
		if (imageId == null ) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		ImageResource imageResource = mappedImageResource.get(resource);
		
		byte[] imageBytes = imageResource.getImage(imageId, extension);

		if (imageBytes == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		BufferedImage image = null;

		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
		} catch (IOException e) {
		}
		
		if (image == null) {
			try {
				response.getOutputStream().write(imageBytes);
				return null;
			} catch (Exception e) {
			}
		}

		if (image == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		boolean needResize = false;
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int resizeToWidth = 0;
		int resizeToHeight = 0;
		
		if (w != null || h != null) {
			if(w != null){
				resizeToWidth = Integer.parseInt(w);
			}else{
				resizeToWidth = imageWidth;
			}
			
			if(h != null){
				resizeToHeight = Integer.parseInt(h);
			}else{
				resizeToHeight = imageHeight;
			}
			
			if(imageWidth != resizeToWidth){
				needResize = true;
			}
			
			if(imageHeight != resizeToHeight){
				needResize = true;
			}
		}
		
		if(needResize){
			//Na primeira etapa, redimensionamos a imagem o m�ximo poss�vel
			int resizeRatio = 0;

			int widthResizeRatio = (100 * (imageWidth - resizeToWidth)) / imageWidth;
			int heightResizeRatio = (100 * (imageHeight - resizeToHeight)) / imageHeight;
			
			resizeRatio = Math.min(widthResizeRatio, heightResizeRatio);

			ScalingMode scalingMode = ScalingMode.BILINEAR;

			if ("true".equalsIgnoreCase(s)) {
				scalingMode = ScalingMode.PROGRESSIVE_BILINEAR;
			}else{
				if(resizeRatio <= 50){
					scalingMode = ScalingMode.BILINEAR;
				}else{
					scalingMode = ScalingMode.PROGRESSIVE_BILINEAR;	
				}
			}
			
			int newHeight = imageHeight - ((imageHeight * resizeRatio) / 100);
			int newWidth = imageWidth - ((imageWidth * resizeRatio) / 100);

			BufferedImage thumbnail = null;
			
			if (z != null) {
				int zoom = Integer.parseInt(z);

				thumbnail = Thumbnails.of(image).size(zoom, zoom).scalingMode(scalingMode).outputQuality(quality).asBufferedImage();
			}else if("true".equalsIgnoreCase(r) && w!=null && h!=null ){
				thumbnail = Thumbnails.of(image).size(Integer.parseInt(w), Integer.parseInt(h)).scalingMode(scalingMode).outputQuality(quality).asBufferedImage();
			} 
			else {
				thumbnail = Thumbnails.of(image).size(newWidth, newHeight).scalingMode(scalingMode).outputQuality(quality).asBufferedImage();
			}

			//Na segunda etapa, posicionamos a imagem no quadro, visto que o redimensionamento nunca ser� perfeito
			int imageX = 0;
			int imageY = 0;

			if (thumbnail.getWidth() > resizeToWidth) {
				imageX = (thumbnail.getWidth() / 2) - (resizeToWidth / 2);
			}

			if (thumbnail.getHeight() > resizeToHeight) {
				imageY = (thumbnail.getHeight() / 2) - (resizeToHeight / 2);
			}

			if (imageX != 0 || imageY != 0) {
				thumbnail = Thumbnails.of(thumbnail).size(resizeToWidth, resizeToHeight).scalingMode(scalingMode).outputQuality(quality).sourceRegion(imageX, imageY, resizeToWidth, resizeToHeight).asBufferedImage();
			}
			
			//imageResource.storeLocalCache(thumbnail, imageId, extension);
			
			Thumbnails
				.of(thumbnail)
				.size(thumbnail.getWidth(), thumbnail.getHeight())
				.scalingMode(scalingMode)
				.outputQuality(quality)
				.outputFormat(extension)
				.toOutputStream(response.getOutputStream());
		}else{
			ImageIO.write(image, extension, response.getOutputStream());
		}
		
		return null;
	}
	
	public static interface ImageResource {
		
		byte[] getImage(String imageId, String extension);

		//void storeLocalCache(BufferedImage image, String imageID, String extension);
		
		long getLastModified(String imageID, String extension);
	}

}
