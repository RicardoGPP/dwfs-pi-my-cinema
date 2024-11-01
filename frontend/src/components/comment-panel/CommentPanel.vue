<template>
    <div class="comment-panel">
        <h1>
            Comentários
        </h1>
        <hr/>
        <LoadingPanel
            v-if="loading"
        />
        <ErrorPanel
            v-else-if="error"
            :message="error"
            :retry="load"
        />
        <div v-else class="content">
            <CommentSummary
                v-if="summary"
                :summary="summary"
            />
            <CommentList
                :comments="comments"
            />
            <CommentInput
                @post="post"
            />
        </div>
    </div>
</template>

<script>
import LoadingPanel from '@/components/loading-panel/LoadingPanel.vue';
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';
import CommentSummary from './CommentPanelCommentSummary.vue';
import CommentList from './CommentPanelCommentList.vue';
import CommentInput from './CommentPanelCommentInput.vue';
import CommentService from '@/services/comment-service';

export default {
    name: 'CommentPanel',
    components: {
        LoadingPanel,
        ErrorPanel,
        CommentSummary,
        CommentList,
        CommentInput
    },
    props: {
        movieId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            comments: [],
            summary: null,
            loading: false,
            error: null
        }
    },
    methods: {
        async load() {
            this.comments = [];
            this.summary = null;
            this.loading = true;
            this.error = null;

            let responses;

            try {
                responses = await Promise.all([
                    CommentService.getAll(this.movieId),
                    CommentService.getSummary(this.movieId)
                ]);
            } catch (error) {
                this.error = error.message;
                return;
            } finally {
                this.loading = false;
            }

            this.comments = responses[0];
            this.summary = responses[1];
        },
        async post(text) {
            const comment = {
                movieId: this.movieId,
                text
            };

            try {
                await CommentService.create(comment);
            } catch (error) {
                //TODO: handle error properly.
                console.error(error.message);
            }

            await this.load();
        }
    },
    created() {
        this.load();
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

hr {
    margin-bottom: 15px;
}
</style>