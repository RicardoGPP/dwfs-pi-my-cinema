<template>
    <div class="movie-panel-view">
        <LifecyclePanel :callback="load">
            <div class="container">
                <div class="backdrop">
                    <img :src="movie.backdropPath"/>
                </div>
                <div class="content">
                    <MoviePanel
                        :movie="movie"
                    />
                    <SessionPanel
                        :movie="movie"
                    />
                    <CommentPanel
                        :movie="movie"
                    />
                </div>
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import MoviePanel from '@/components/movie-panel/MoviePanel.vue';
import SessionPanel from '@/components/session-panel/SessionPanel.vue';
import CommentPanel from '@/components/comment-panel/CommentPanel.vue';
import MovieService from '@/services/movie-service';

export default {
    name: 'MovieDetailView',
    components: {
        LifecyclePanel,
        MoviePanel,
        SessionPanel,
        CommentPanel
    },
    props: {
        id: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            movie: null
        }
    },
    methods: {
        async load() {
            this.movie = await MovieService.getById(this.id);
        }
    }
}
</script>

<style lang="scss" scoped>
.movie-panel-view {
    display: flex;
    justify-content: center;
}

.container {
    position: relative;
    display: flex;
    justify-content: center;
    width: 100%;
}

.backdrop {
    position: absolute;
    width: 100%;
    height: 300px;
    overflow: hidden;

    & img {
        display: block;
        width: 100%;
        height: 100%;
        object-fit: cover;
        opacity: 0.2;
    }

    &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 50%;
        background: linear-gradient(to bottom, #00000000 0%, #00000099 100%);
        z-index: 1;
    }
}

.content {
    position: absolute;
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 1024px;
    padding: 20px;
    margin-top: 80px;
}

.error {
    color: #ff0000;
}

@media (max-width: 1024px) {
    .backdrop {
        height: 680px;
    }

    .content {
        width: 100%;
    }
}
</style>