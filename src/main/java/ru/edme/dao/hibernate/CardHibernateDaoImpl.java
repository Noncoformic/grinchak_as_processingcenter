package ru.edme.dao.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.util.List;

public class CardHibernateDaoImpl implements Dao<Card> {
    private static final Logger logger = LogManager.getLogger(CardHibernateDaoImpl.class);

    @Override
    public void createTable() {

    }

    @Override
    public void dropTable() {

    }

    @Override
    public void clearTable() {
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from Сard").executeUpdate();
            transaction.commit();
            logger.info("Cards have been deleted.");
        }catch(Exception e){
            logger.error("Error clearing card",e);
        }

    }

    @Override
    public void insert(Card entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Card> getAll() {
        return List.of();
    }

    @Override
    public Card getById(Long id) {
        return null;
    }

    @Override
    public void update(Card card) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(card);
            transaction.commit();
            System.out.println("✅ Card updated: " + card.getCardNumber());
        } catch (Exception e) {
            throw new RuntimeException("Error updating Card", e);
        }

    }
}
