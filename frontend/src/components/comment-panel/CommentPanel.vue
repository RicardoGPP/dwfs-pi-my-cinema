<template>
    <div class="comment-panel">
        <h1>
            Comentários
        </h1>
        <hr/>
        <LifecyclePanel :callback="load" :signal="signal">
            <div class="content">
                <InfoPanel
                    v-if="summary"
                    title="Resumo dos comentários"
                    :message="summary"
                />
                <CommentList
                    :comments="comments"
                />
                <CommentInput
                    v-if="user"
                    @post="post"
                />
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import InfoPanel from '@/components/info-panel/InfoPanel.vue';
import CommentList from './CommentPanelCommentList.vue';
import CommentInput from './CommentPanelCommentInput.vue';
import CommentService from '@/services/comment-service';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';
import AuthMixin from '@/mixins/auth-mixin';

export default {
    name: 'CommentPanel',
    mixins: [
        LifecycleSignalMixin,
        AuthMixin
    ],
    components: {
        LifecyclePanel,
        InfoPanel,
        CommentList,
        CommentInput
    },
    props: {
        movie: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            comments: [],
            summary: null
        }
    },
    methods: {
        reset() {
            this.comments = [];
            this.summary = null;
        },
        async load() {
            this.reset();

            const promises = [];

            promises.push(CommentService.getAll(this.movie.id));
            promises.push(CommentService.getSummary(this.movie.id));

            const responses = await Promise.all(promises);

            this.comments = responses[0];
            this.summary = responses[1];
        },
        async post(text) {
            const comment = {
                movieId: this.movie.id,
                text
            };

            try {
                await CommentService.create(comment);
            } catch (error) {
                console.error(error.message);
                return;
            }

            this.doSignal();
        }
    }
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

hr {
    margin-bottom: 30px;
}
</style>