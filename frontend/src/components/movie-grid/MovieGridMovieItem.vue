<template>
    <div
        class="movie-grid-movie-item"
        @click="goToMovieDetail">
        <div class="poster">
            <img
                :src="path"
                :alt="movie.title"
                :title="movie.title"
            />
        </div>
        <div class="footer">
            <div class="footer-overlay"/>
            <div class="footer-text">
                {{ movie.title }}
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MovieGridMovieItem',
    props: {
        movie: {
            type: Object,
            required: true
        }
    },
    computed: {
        path() {
            return `https://image.tmdb.org/t/p/w500/${this.movie.posterPath}`;
        }
    },
    methods: {
        goToMovieDetail() {
            this.$router.push({
                name: 'movie-detail',
                params: {
                    id: this.movie.id
                }
            });
        }
    }
}
</script>

<style lang="scss" scoped>
.movie-grid-movie-item {
    position: relative;
    width: 300px;
    height: 450px;
    overflow: hidden;
    border-radius: 5px;
    cursor: pointer;

    &:hover {
        border: 3px solid #00FF7F;

        & > .footer {
            display: block;
        }
    }
}

.poster {
    position: absolute;
    width: 100%;
    height: 100%;

    & > img {
        width: 100%;
        height: 100%;
    }
}

.footer {
    display: none;
    position: absolute;
    width: 100%;
    min-height: 60px;
    bottom: 0;
}

@media (max-width: 1280px) {
    .footer {
        display: block;
    }
}

.footer-overlay {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: #000000;
    opacity: 0.8;
}

.footer-text {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    width: 100%;
    height: 100%;
    padding: 10px;
    color: #ffffff;
    font-size: 18px;
}
</style>