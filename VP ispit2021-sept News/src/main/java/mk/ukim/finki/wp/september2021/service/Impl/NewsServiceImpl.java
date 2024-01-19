package mk.ukim.finki.wp.september2021.service.Impl;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsCategoryService newsCategoryService;

    public NewsServiceImpl(NewsRepository newsRepository, NewsCategoryService newsCategoryService) {
        this.newsRepository = newsRepository;
        this.newsCategoryService = newsCategoryService;
    }

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(()->new InvalidNewsIdException(id));
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory=newsCategoryService.findById(category);
        return newsRepository.save(new News(name,description,price,type,newsCategory));
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory=newsCategoryService.findById(category);
        News news = newsRepository.findById(id)
                .orElseThrow(()->new InvalidNewsIdException(id));
        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        news.setCategory(newsCategory);
        return this.newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(()->new InvalidNewsIdException(id));
        newsRepository.delete(news);
        return news;
    }

    @Override
    public News like(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(()->new InvalidNewsIdException(id));
        news.setLikes(news.getLikes()+1);
        return newsRepository.save(news);
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if(price == null && type == null){
            return newsRepository.findAll();
        }
        if(price == null){
            return newsRepository.findAllByTypeEquals(type);
        }
        if(type == null){
            return newsRepository.findAllByPriceLessThan(price);
        }
        return newsRepository.findAllByPriceLessThanAndTypeEquals(price, type);
    }
}
