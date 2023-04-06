package com.example.catpicture.infra.client;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.catpicture.domain.CatPictureRepository;
import com.example.catpicture.domain.entity.CatPicture;
import com.example.catpicture.infra.dto.ClientPictureResponse;

@Component
public class CatPictureClientRunner implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(CatPictureClientRunner.class);

	private final CatPictureRepository catPictureRepository;
	private final CatPictureLoader catPictureLoader;

	public CatPictureClientRunner(
		CatPictureRepository catPictureRepository,
		CatPictureLoader catPictureLoader
	) {
		this.catPictureRepository = catPictureRepository;
		this.catPictureLoader = catPictureLoader;
	}

	@Override
	public void run(String... args) {
		List<CatPicture> catPictures = loadData();

		log.info("{}", catPictures);

		catPictureRepository.saveAll(catPictures);
	}

	private List<CatPicture> loadData() {
		return catPictureLoader.loadBreeds()
			.stream()
			.map(breedDetails -> ClientPictureResponse.toEntity(
				catPictureLoader.loadPictures(breedDetails),
				breedDetails
			))
			.flatMap(Collection::stream)
			.toList();
	}
}