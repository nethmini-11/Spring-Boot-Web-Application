package com.ceylonomic.store.store.service;

import com.ceylonomic.store.store.domain.Article;
import com.ceylonomic.store.store.domain.CartItem;
import com.ceylonomic.store.store.domain.ShoppingCart;
import com.ceylonomic.store.store.domain.User;


public interface ShoppingCartService {

	ShoppingCart getShoppingCart(User user);
	
	int getItemsNumber(User user);
	
	CartItem findCartItemById(Long cartItemId);
	
	CartItem addArticleToShoppingCart(Article article, User user, int qty, String size);
		
	void clearShoppingCart(User user);
	
	void updateCartItem(CartItem cartItem, Integer qty);

	void removeCartItem(CartItem cartItem);
	
}
