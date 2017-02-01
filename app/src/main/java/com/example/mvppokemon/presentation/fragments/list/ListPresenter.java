package com.example.mvppokemon.presentation.fragments.list;

import com.example.mvppokemon.dagger.qualifier.Repository;
import com.example.mvppokemon.data.models.PokemonModel;
import com.example.mvppokemon.data.repositories.pokemon.interfaces.PokemonRepositoryInterface;
import com.example.mvppokemon.presentation.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;

public final class ListPresenter extends BasePresenter<ListView> implements ListPresenterInterface {

    // The view is available using the view variable
    PokemonRepositoryInterface pokemonRepository;

    @Inject
    public ListPresenter(@Repository PokemonRepositoryInterface pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);
        // Your code here. Your view is available using view and will not be null until next onStop()
        getAllLocalPokemon();
    }

    private void setPokemonList(List<PokemonModel> pokemon) {
        if (pokemon != null) {
            if (view != null) {
                view.setAdapterData(pokemon);
            }
        }
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */
        super.onPresenterDestroyed();
    }

    @Override
    public void refreshData() {
        getAllLocalPokemon();
    }

    private void getAllLocalPokemon() {
        pokemonRepository.getAllLocalPokemon().subscribe(new DisposableSingleObserver<List<PokemonModel>>() {
            @Override
            public void onSuccess(List<PokemonModel> value) {
                if (view != null) {
                    view.hideSwipeRefreshLoader();
                }

                setPokemonList(value);
            }

            @Override
            public void onError(Throwable e) {
                if (view != null) {
                    view.hideSwipeRefreshLoader();
                }
            }
        });
    }
}