package com.lyl.husky.console.respository.impl;

import com.lyl.husky.console.exception.JobConsoleException;
import com.lyl.husky.console.respository.XmlRepository;
import com.lyl.husky.console.util.HomeFolderUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * 基于XML的数据访问器
 * @param <E>
 */
public abstract class AbstractXmlRepositoryImpl<E> implements XmlRepository<E> {

    private final File file;

    private final Class<E> clazz;

    private JAXBContext jaxbContext;

    protected AbstractXmlRepositoryImpl(final String fileName, final Class<E> clazz){
        file = new File(HomeFolderUtils.getFilePathInHomeFolder(fileName));
        this.clazz = clazz;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
        } catch (final JAXBException ex){
            throw new JobConsoleException(ex);
        }
    }

    @Override
    public synchronized E load() {
        if (!file.exists()){
            try {
                return clazz.newInstance();
            } catch (final InstantiationException | IllegalAccessException ex){
                throw new JobConsoleException(ex);
            }
        }
        try {
            @SuppressWarnings("unchecked")
            E result = (E) jaxbContext.createUnmarshaller().unmarshal(file);
            return result;
        } catch (final JAXBException ex){
            throw new JobConsoleException(ex);
        }
    }

    @Override
    public synchronized void save(final E entity) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(entity, file);
        } catch (final JAXBException ex){
            throw new JobConsoleException(ex);
        }
    }
}
