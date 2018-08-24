package ru.job4j.sellboard.model.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.sellboard.model.dao.util.DaoUtil;
import ru.job4j.sellboard.model.entity.Ad;
import ru.job4j.sellboard.model.entity.enums.Sort;

import java.util.List;

/**
 * @author Yury Matskevich
 */
public class AdDAOImpl extends GenericDAOImpl<Ad, Integer> implements AdDAO {
	@Override
	@SuppressWarnings("unchecked")
	public List<Ad> findAllAd() {
		Session session = getSession();
		return session.createQuery("from Ad").getResultList();
	}

	@Override
	public void delteAd(int id) {
		Session session = getSession();
		Query query = session.createQuery("delete Ad where id =: paramID");
		query.setParameter("paramID", id);
		query.executeUpdate();
	}

	@Override
	public Ad findById(int id) {
		Session session = getSession();
		Query query = session.createQuery("from Ad where id =: paramID");
		query.setParameter("paramID", id);
		return (Ad) query.getSingleResult();
	}

	@Override
	public void updateAd(Ad ad) {
		Session session = getSession();
		Ad oldAd = session.get(Ad.class, ad.getId());
		oldAd.setId(ad.getId());
		oldAd.setDescribe(ad.getDescribe());
		oldAd.setPrice(ad.getPrice());
		oldAd.setState(ad.getState());
		oldAd.setCar(ad.getCar());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ad> findWithPhoto() {
		Session session = getSession();
		Query query = session.createQuery("from Ad where picture is not null");
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ad> findTodayAd() {
		Session session = getSession();
		Query query = session.createQuery("from Ad where time >: paramTime");
		query.setParameter("paramTime", DaoUtil.getStartOfDay());
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ad> findBySort(Sort sort) {
		Session session = getSession();
		Query query = session.createQuery("from Ad where car.sort =: paramSort");
		query.setParameter("paramSort", sort);
		return query.getResultList();
	}
}