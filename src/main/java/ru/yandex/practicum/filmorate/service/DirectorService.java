package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.dao.DirectorDao;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    @Autowired
    @Qualifier("directorDaoImpl")
    private DirectorDao directorStorage;

    public Director create(Director director) throws ValidationException {
        validateDirector(director);
        directorStorage.createDirector(director);
        return director;
    }

    public Director update(Director director) throws ValidationException, EntityNotFoundException {
        validateDirector(director);
        directorStorage.getDirectorById(director.getId()).orElseThrow(() -> new EntityNotFoundException(
                String.format("Режиссера с id=%d не существует", director.getId())));
        return directorStorage.updateDirector(director);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        if (directorStorage.getDirectorById(id).isPresent()) {
            log.info(String.format("Режиссер с id=%d удален:", id) + directorStorage.getDirectorById(id));
            directorStorage.deleteDirector(id);
        } else {
            throw new EntityNotFoundException(String.format("Режиссера с id=%d не существует", id));
        }
    }

    public List<Director> findAll() {
        log.info("Список режиссеров получен: " + directorStorage.getAllDirectors());
        return directorStorage.getAllDirectors();
    }

    public Director findById(int id) throws EntityNotFoundException {
        return directorStorage.getDirectorById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Режиссера с id=%d не существует", id)));
    }

    public void validateDirector(Director director) throws ValidationException {
        if (!StringUtils.hasText(director.getName().trim())) {
            throw new ValidationException("Имя режиссера не может быть пустым");
        }
    }
}
