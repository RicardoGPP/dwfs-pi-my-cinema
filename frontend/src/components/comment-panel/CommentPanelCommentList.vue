<template>
    <div class="comment-panel-comment-list">
        <span v-if="comments.length === 0">
            Este filme ainda não possui comentários. Seja o primeiro a comentar!
        </span>
        <div v-else>
            <CommentItem
                v-for="(comment, index) in comments"
                :key="index"
                :comment="comment"
            />
        </div>
    </div>
</template>

<script>
import CommentItem from './CommentPanelCommentListCommentItem.vue';

export default {
    name: 'CommentPanelCommentList',
    components: {
        CommentItem
    },
    props: {
        comments: {
            type: Array,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-list {

    padding: 20px;
    border: 1px solid gray;
    border-radius: 5px;

    & > div {
        display: flex;
        flex-direction: column;
        gap: 30px;
    }
}
</style>