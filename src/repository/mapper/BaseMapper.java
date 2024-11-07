package repository.mapper;

import model.BaseEntity;

public interface BaseMapper<T extends BaseEntity> {
    T fromCsvString(String csvString);
}