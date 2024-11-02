<template>
    <div class="comment-panel-comment-list">
        <div v-if="comments.length === 0" class="message">
            Este filme ainda não possui comentários.
        </div>
        <div v-else class="content">
            <CommentItem
                v-for="(comment, index) in comments"
                :key="index"
                :comment="comment"
                :odd="index % 2 === 0"
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
    border: 1px solid #4f4f4f;
    border-radius: 5px;
    overflow: hidden;
}

.message {
    margin: 20px;
}

.content {
    display: flex;
    flex-direction: column;
}
</style>